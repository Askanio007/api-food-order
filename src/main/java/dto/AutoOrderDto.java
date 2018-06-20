package dto;

import entity.AutoOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class AutoOrderDto {

    private long idUser;

    private boolean active;

    private String type;

    private List<FoodDto> foods;

    private AutoOrderDto() {}

    private AutoOrderDto(AutoOrder autoOrder) {
        this.active = autoOrder.isActive();
        this.idUser = autoOrder.getUser().getId();
        this.type = autoOrder.getType();
        this.foods = FoodDto.convertToDto(autoOrder.getFoods());
    }

    public static AutoOrderDto convertToDto(AutoOrder autoOrder) {
        return new AutoOrderDto(autoOrder);
    }

    public static List<AutoOrderDto> convertToDto(List<AutoOrder> autoOrders) {
        ArrayList<AutoOrderDto> list = new ArrayList<>();
        autoOrders.forEach(autoOrder -> list.add(convertToDto(autoOrder)));
        return list;
    }

}
