package food_metrics.api.Food.Repository;

import food_metrics.api.Food.Model.PreparationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreparationRepository extends JpaRepository<PreparationModel, Long> {

    boolean existsByMethod(String method);
}
