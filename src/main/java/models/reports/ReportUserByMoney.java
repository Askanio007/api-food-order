package models.reports;

import converter.MoneyToString;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class ReportUserByMoney {

    private long id;

    private String login;

    private String name;

    @Setter(AccessLevel.NONE)
    private String money;

    public ReportUserByMoney() {}

    public void setMoney(BigDecimal money) {
        this.money = MoneyToString.convert(money);
    }
}
