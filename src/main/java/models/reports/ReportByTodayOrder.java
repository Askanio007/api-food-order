package models.reports;

import entity.Food;
import entity.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReportByTodayOrder {

    private String login;

    private String name;

    @Setter(AccessLevel.NONE)
    private List<String> foodName;

    public void setFoodName(Order order) {
        this.foodName = new ArrayList<>();
        if (order == null)
            return;
        for (Food food : order.getFoods()) {
            this.foodName.add(food.getName());
        }
    }

    public ReportByTodayOrder() {}

    public ReportByTodayOrder(String name, Order order) {
        this.name = name;
        setFoodName(order);
    }
}
