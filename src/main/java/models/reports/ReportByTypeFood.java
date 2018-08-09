package models.reports;

import converter.MoneyToString;
import dto.FoodTypeDto;
import entity.FoodType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class ReportByTypeFood {

    private String type;

    private long count;

    private String price;

    public ReportByTypeFood() {}

    public ReportByTypeFood(String type, long count, BigDecimal price) {
        this.type = type;
        this.count = count;
        this.price = MoneyToString.convert(price);
    }

    public String getType() {
        return this.type;
    }
}
