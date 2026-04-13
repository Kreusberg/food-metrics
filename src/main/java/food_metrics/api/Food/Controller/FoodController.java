package food_metrics.api.Food.Controller;

import food_metrics.api.Food.Model.NutrientModel;
import food_metrics.api.Food.Model.RequestModel;
import food_metrics.api.Food.Service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping("nutrients/")
    public List<NutrientModel> getNutrientsInfoList(@RequestBody RequestModel param) throws IOException, InterruptedException {
        return foodService.getNutrientsInfoList(param);
    }

    @PostMapping("nutrients/{id}")
    public Optional<NutrientModel> getNutrientsInformationList(@RequestBody RequestModel param, @PathVariable int id) throws IOException, InterruptedException {
        return foodService.getNutrientInfoFromId(param, id);
    }
}
