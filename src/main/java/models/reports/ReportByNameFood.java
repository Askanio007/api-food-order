package models.reports;

import converter.MoneyToString;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class ReportByNameFood {

    private String productCode;

    private String name;

    private long count;

    private String price;

    private String fullPrice;

    public ReportByNameFood() {}

    public ReportByNameFood(String code, String name, long count, BigDecimal price,  BigDecimal fullPrice) {
        this.productCode = code;
        this.name = name;
        this.count = count;
        this.price = MoneyToString.convert(price);
        this.fullPrice = MoneyToString.convert(fullPrice);
    }
}