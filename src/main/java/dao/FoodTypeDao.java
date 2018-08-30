package dao;

import dto.FoodTypeDto;
import entity.FoodType;

import java.util.List;

public interface FoodTypeDao extends GenericDao<FoodType> {

    FoodType find(String type);

    List<FoodType> listPermanentTypes();

    List<FoodType> listComboTypes();

    boolean typeExist(String type);
}
