package converter;

import java.math.BigDecimal;

public class MoneyToString {

    public static String convert(BigDecimal money) {
        if (money != null)
            return money.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        return "0.00";
    }
}
