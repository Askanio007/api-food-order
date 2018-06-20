package models.reports;

import converter.MoneyToString;
import lombok.Getter;
import lombok.Setter;
import converter.DateConverter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class ReportOrderPriceByDate {

    private String date;

    private String price;

    public ReportOrderPriceByDate() {}

    public ReportOrderPriceByDate(Date date, BigDecimal price) {
        this.date = DateConverter.getFormatView().format(date);
        this.price = MoneyToString.convert(price);
    }
}
