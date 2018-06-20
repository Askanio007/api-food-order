package dao;

import entity.FoodType;

import java.util.List;

public interface FoodTypeDao extends GenericDao<FoodType> {

    FoodType find(enums.FoodType type);

    List<FoodType> listPermanentTypes();
}
