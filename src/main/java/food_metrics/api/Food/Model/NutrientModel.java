package food_metrics.api.Food.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NutrientModel {

    private int nutrientId;
    private String nutrientName;
    private String nutrientNumber;
    private String unitName;
    private double value;
    private int rank;
    private int indentLevel;
    private long foodNutrientId;

}
