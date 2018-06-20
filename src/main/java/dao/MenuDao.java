package dao;

import entity.Menu;
import entity.Order;

import java.util.List;

public interface MenuDao extends GenericDao<Menu> {

    boolean todayMenuExist();

    boolean todayMenuIsActive();

    Menu getTodayMenu();

    void deleteTodayMenu(long id);

    List<Menu> getTodayMenues();
}
