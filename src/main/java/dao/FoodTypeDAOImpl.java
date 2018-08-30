package dao;

import entity.FoodType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("foodTypeDao")
public class FoodTypeDAOImpl extends GenericDAOImpl<FoodType> implements FoodTypeDao {
    @Override
    public FoodType find(String type) {
        return (FoodType)createQuery("from FoodType where type = :type").setParameter("type", type).uniqueResult();
    }

    @Override
    public List<FoodType> listPermanentTypes() {
        return (List<FoodType>)createQuery("select ft from FoodType as ft join ft.foods as food where ft.combo = false and food.active = true group by ft")
                .list();
    }

    @Override
    public List<FoodType> listComboTypes() {
        return (List<FoodType>)createQuery("select ft from FoodType as ft join ft.foods as food where food.active = true and ft.combo = true group by ft")
                .list();
    }

    @Override
    public boolean typeExist(String type) {
        return false;
    }
}
