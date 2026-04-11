package food_metrics.api.Food.Service;

import food_metrics.api.Food.Model.NutrientModel;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class FoodService {

    public List<NutrientModel> getNutrientsInformationList(String foodName) throws IOException, InterruptedException {

        ObjectMapper mapper = new ObjectMapper();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
           .uri(URI.create("https://api.nal.usda.gov/fdc/v1/foods/search?query=" + foodName + "%2Craw&api_key=" + System.getenv("FOOD_METRICS_TOKEN") + "&pageSize=3"))
           .header("Content-Type", "application/json")
           .GET()
           .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        JsonNode root = mapper.readTree(response.body());

        JsonNode value = root.path("foods").get(0).path("foodNutrients");

       List<NutrientModel> list = mapper.readerForListOf(NutrientModel.class).readValue(value);

       List<Integer> nutrientsId = new ArrayList<>();
       nutrientsId.add(1003);
       nutrientsId.add(1004);
       nutrientsId.add(1005);
       nutrientsId.add(1008);
       nutrientsId.add(2000);

       return list.stream().filter(c -> nutrientsId.contains(c.getNutrientId())).toList();
    }
}























