package dao;

import entity.Menu;
import entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

import static utils.DateBuilder.*;

@Repository("menuDao")
public class MenuDAOImpl extends GenericDAOImpl<Menu> implements MenuDao {

    @Override
    public boolean todayMenuExist() {
        long i = (long) createQuery("select count(*) from Menu where dateRegistrationMenu between :startDay and :endDay")
                .setParameter("startDay", startDay(today()))
                .setParameter("endDay", endDay(today()))
                .uniqueResult();
        return (i == 1);
    }

    @Override
    public Menu getTodayMenu() {
        return (Menu) createQuery("from Menu where dateRegistrationMenu between :startDay and :endDay")
                .setParameter("startDay", startDay(today()))
                .setParameter("endDay", endDay(today()))
                .uniqueResult();
    }

    @Override
    public boolean todayMenuIsActive() {
        Object act = createQuery("select active from Menu where dateRegistrationMenu between :startDay and :endDay")
                .setParameter("startDay", startDay(today()))
                .setParameter("endDay", endDay(today()))
                .uniqueResult();
        return act != null && (boolean) act;
    }

    @Override
    public void deleteTodayMenu(long id) {
        session().createNativeQuery("DELETE FROM menu_foods WHERE menu_id = :id").setParameter("id", id).executeUpdate();
        session().createNativeQuery("DELETE FROM menu WHERE id = :id").setParameter("id", id).executeUpdate();
    }

    @Override
    public List<Menu> getTodayMenues() {
        List<Menu> menues = createQuery("from Menu where dateRegistrationMenu between :from and :to")
                .setParameter("from", startDay(today()))
                .setParameter("to", endDay(today()))
                .list();
        return menues;
    }
}
