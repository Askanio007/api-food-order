package dao;

import dto.FoodTypeDto;
import entity.CompletedOrderItem;
import entity.Food;

import java.math.BigDecimal;
import java.util.List;

public interface FoodDao extends GenericDao<Food> {

    List<CompletedOrderItem> findById(long completedOrderId);

    List<Food> findActive();

    Food find(String name);

    List<Food> findOther();

    List<Food> findAvailable(String type);

    BigDecimal priceFood(long id);

    Food findByProductCode(String code);

}
