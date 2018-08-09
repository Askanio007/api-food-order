package dto;

import converter.MoneyToString;
import entity.CompletedOrderItem;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CompletedOrderItemDto {

    private long id;

    private String type;

    private String name;

    private String priceView;

    private String productCode;

    private BigDecimal price;

    private CompletedOrderItemDto() {
    }

    private CompletedOrderItemDto(CompletedOrderItem food) {
        this.id = food.getId();
        this.name = food.getName();
        this.type = food.getType();
        this.price = food.getPrice();
        this.priceView = MoneyToString.convert(food.getPrice());
        this.productCode = food.getProductCode();
    }

    public static CompletedOrderItemDto convertToDto(CompletedOrderItem food) {
        return new CompletedOrderItemDto(food);
    }

    public static List<CompletedOrderItemDto> convertToDto(Collection<CompletedOrderItem> foods) {
        List<CompletedOrderItemDto> foodDto = new ArrayList<>();
        if (foods != null && !foods.isEmpty())
            foods.forEach(food -> foodDto.add(convertToDto(food)));
        return foodDto;
    }

}
