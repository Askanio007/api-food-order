package dto;

import entity.Menu;
import lombok.Getter;
import lombok.Setter;
import utils.DateBuilder;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class MenuDto {

    private long id;

    private String dateRegMenu;

    private long dateAcceptOrder;

    private Date dateAcceptOrderD;

    private boolean active;

    private List<FoodDto> foods;

    private MenuDto() {}

    public MenuDto(Menu menu) {
        this.id = menu.getId();
        this.dateRegMenu = menu.getDateRegistrationMenu().toString();
        this.dateAcceptOrder = menu.getDateAcceptingOrder().getTime();
        this.dateAcceptOrderD = menu.getDateAcceptingOrder();
        this.active = menu.isActive();
        if (!menu.getFoods().isEmpty()) {
            this.foods = FoodDto.convertToDto(menu.getFoods());
        }
    }

    public static MenuDto convertToDto(Menu menu) {
        return new MenuDto(menu);
    }

    public void setDateAcceptOrderD(String date) {
        this.dateAcceptOrderD = DateBuilder.timeAcceptOrders(date);
    }

}
