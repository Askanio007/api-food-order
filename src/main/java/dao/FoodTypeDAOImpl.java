package dao;

import entity.FoodType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("foodTypeDao")
public class FoodTypeDAOImpl extends GenericDAOImpl<FoodType> implements FoodTypeDao {
    @Override
    public FoodType find(enums.FoodType type) {
        return (FoodType)createQuery("from FoodType where type = :type").setParameter("type", type).uniqueResult();
    }

    @Override
    public List<FoodType> listPermanentTypes() {
        return (List<FoodType>)createQuery("select ft from FoodType as ft join ft.foods as food where food.availableEveryDay = true and food.active = true group by ft")
                .list();
    }
}
