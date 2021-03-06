package dao;

import entity.CompletedOrderItem;
import entity.Food;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository("foodDao")
public class FoodDAOImpl extends GenericDAOImpl<Food> implements FoodDao {

    public List<CompletedOrderItem> findById(long orderId) {
        Object foods = createQuery("select f from CompletedOrderItem as f join f.completedOrder as o where o.id = :id ")
                .setParameter("id", orderId)
                .list();
        return (List<CompletedOrderItem>) foods;
    }

    @Override
    public Food find(String name) {
        Food food = (Food) createQuery("from Food where name = :name ")
                .setParameter("name", name)
                .uniqueResult();
        return food;
    }

    @Override
    public List<Food> findOther() {
        Object foods = createQuery("select f from Food as f join f.type as type where type.type not like :type and f.active = true")
                .setParameter("type", "%COMBO%")
                .list();
        return (List<Food>) foods;
    }

    @Override
    public List<Food> findAvailable(String type) {
        Object foods = createQuery("select f from Food as f join f.type as type where type.type = :type and f.active = true and f.availableEveryDay = true")
                .setParameter("type", type)
                .list();
        return (List<Food>) foods;
    }

    @Override
    public List<Food> findActive() {
        Object foods = createQuery("from Food where active = true order by name asc").list();
        return (List<Food>) foods;
    }

    @Override
    public BigDecimal priceFood(long id) {
        Object price = createQuery("select price from Food where id = :id")
                .setParameter("id", id)
                .uniqueResult();
        return getNumber(price);
    }

    @Override
    public Food findByProductCode(String code) {
        return (Food) createQuery("from Food where productCode = :code ")
                .setParameter("code", code)
                .uniqueResult();
    }
}
