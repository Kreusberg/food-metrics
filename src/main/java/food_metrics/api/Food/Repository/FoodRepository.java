package food_metrics.api.Food.Repository;

import food_metrics.api.Food.Model.FoodModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodModel, Long> {
}
