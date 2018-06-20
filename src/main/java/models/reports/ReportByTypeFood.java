package models.reports;

import converter.MoneyToString;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class ReportByTypeFood {

    private enums.FoodType type;

    private long count;

    private String price;

    public ReportByTypeFood() {}

    public ReportByTypeFood(enums.FoodType type, long count, BigDecimal price) {
        this.type = type;
        this.count = count;
        this.price = MoneyToString.convert(price);
    }

    public String getType() {
        return enums.FoodType.getType(this.type);
    }
}
