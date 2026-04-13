package food_metrics.api.Food.Service;

import food_metrics.api.Food.Model.FoodModel;
import food_metrics.api.Food.Model.NutrientModel;
import food_metrics.api.Food.Model.PreparationModel;
import food_metrics.api.Food.Model.RequestModel;
import food_metrics.api.Food.Repository.FoodRepository;
import food_metrics.api.Food.Repository.PreparationRepository;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final PreparationRepository preparationRepository;

    public FoodService(FoodRepository foodRepository, PreparationRepository preparationRepository) {
        this.foodRepository = foodRepository;
        this.preparationRepository = preparationRepository;
    }

    ObjectMapper mapper = new ObjectMapper();
    HttpClient client = HttpClient.newHttpClient();



    public List<NutrientModel> getNutrientsInfoList(RequestModel param) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.nal.usda.gov/fdc/v1/foods/search?query=" + param.getFoodName() +
                        "%2C" + param.getMethod() +
                        "&dataType=Foundation" +
                        "&api_key=" + System.getenv("FOOD_METRICS_TOKEN") + "&pageSize=3"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode root = mapper.readTree(response.body());

        JsonNode foodsList = root.path("foods");

        List<Integer> nutrientsId = List.of(1003, 1004, 1005, 1008, 2000);

        for (JsonNode item : foodsList) {
            if (item.path("description").asText().toLowerCase().startsWith(param.getFoodName().toLowerCase())) {
                JsonNode nutrientsList = item.path("foodNutrients");
                List<NutrientModel> filteredNutrients = mapper.readerForListOf(NutrientModel.class).readValue(item.path("foodNutrients"));

                if (shouldAddFood(param.getFoodName())) {
                    addFood(FoodModel.builder()
                            .foodName(param.getFoodName())
                            .foodId(item.path("fdcId").asInt())
                            .build());
                }

                if (shouldAddPreparation(param.getMethod())) {
                    addPreparation(PreparationModel.builder().method(param.getMethod()).build());
                }

                return filteredNutrients.stream().filter(c -> nutrientsId.contains(c.getNutrientId())).toList();
            }
        }

        return mapper.readerForListOf(NutrientModel.class).readValue(foodsList.get(0).path("foodNutrients"));
    }

    public Optional<NutrientModel> getNutrientInfoFromId(RequestModel param, int id) throws IOException, InterruptedException {
        List<NutrientModel> nutrients = getNutrientsInfoList(param);

        return Optional.of(nutrients.stream().filter(c -> c.getNutrientId() == id).findFirst().orElse(new NutrientModel()));
    }

    private boolean shouldAddFood(String foodName) {
        return !foodRepository.existsByFoodName(foodName);
    }

    private boolean shouldAddPreparation(String preparationName) {
        return !preparationRepository.existsByMethod(preparationName);
    }

    private void addFood(FoodModel food) {
        foodRepository.save(food);
    }

    private void addPreparation(PreparationModel preparation) {
        preparationRepository.save(preparation);
    }
}























