package dto;

import converter.MoneyToString;
import entity.Food;
import enums.FoodType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter @Setter
public class FoodDto {

    private long id;

    private String type;

    @NonNull
    @Length(min = 1)
    private String name;

    private String priceView;

    @NonNull
    @Length(min = 1)
    private String productCode;

    @NonNull
    @DecimalMin(value = "1")
    private BigDecimal price;

    private boolean available;

    private FoodDto() {
    }

    private FoodDto(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.type = FoodType.getType(food.getType().getType());
        this.price = food.getPrice();
        this.priceView = MoneyToString.convert(food.getPrice());
        this.available = food.isAvailableEveryDay();
        this.productCode = food.getProductCode();
    }

    public static FoodDto convertToDto(Food food) {
        return new FoodDto(food);
    }

    public static List<FoodDto> convertToDto(Collection<Food> foods) {
        List<FoodDto> foodDto = new ArrayList<>();
        if (foods != null && !foods.isEmpty())
            foods.forEach(food -> foodDto.add(convertToDto(food)));
        return foodDto;
    }

    public static List<FoodDto> listByType(Collection<FoodDto> foods, String type) {
        foods.removeIf(food -> !food.getType().equals(type));
        return (List<FoodDto>)foods;
    }
}
