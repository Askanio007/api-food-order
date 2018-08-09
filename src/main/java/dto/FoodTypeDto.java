package dto;

import entity.FoodType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FoodTypeDto {

    private long id;

    private boolean combo;

    private String type;

    private FoodTypeDto() {}

    private FoodTypeDto(FoodType fType) {
        this.id = fType.getId();
        this.type =fType.getType();
        this.combo = fType.isCombo();
    }

    public static FoodTypeDto convertToDto(FoodType fType) {
        return new FoodTypeDto(fType);
    }

    public static List<String> convertToDto(List<FoodType> fTypes) {
        List<String> foodTypeDto = new ArrayList<>();
        fTypes.forEach(foodType -> foodTypeDto.add(foodType.getType()));
        return foodTypeDto;
    }

    public static List<FoodTypeDto> convertToDtos(List<FoodType> fTypes) {
        List<FoodTypeDto> foodTypeDto = new ArrayList<>();
        fTypes.forEach(foodType -> foodTypeDto.add(convertToDto(foodType)));
        return foodTypeDto;
    }
}
