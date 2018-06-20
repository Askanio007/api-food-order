package dao;

import entity.Food;
import entity.FoodType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface FoodDao extends GenericDao<Food> {

    List<Food> findById(long orderId);

    List<Food> findActive();

    Food find(String name);

    List<Food> findOther();

    List<Food> findAvailable(String type);

    BigDecimal priceFood(long id);

    Food findByProductCode(String code);

}
