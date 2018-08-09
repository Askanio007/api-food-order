package service;

import dao.MenuDao;
import dto.MenuDto;
import entity.Food;
import entity.Menu;
import enums.StatusOrder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import converter.DateConverter;
import scheduler.NotifyAboutMenu;
import scheduler.NotifyAboutOrder;
import scheduler.SendOrderInFoodProvider;
import utils.DateBuilder;

import javax.transaction.Transactional;
import java.util.*;

import static utils.DateBuilder.today;

@Service
public class MenuService {

    private static final Logger log = Logger.getLogger(MenuService.class);

    @Autowired
    @Qualifier("menuDao")
    private MenuDao menuDao;

    @Autowired
    @Qualifier("myScheduler")
    private TaskScheduler scheduler;

    @Autowired
    private SendOrderInFoodProvider sendOrderInFoodProvider;

    @Autowired
    private NotifyAboutMenu notifyAboutMenu;

    @Autowired
    private NotifyAboutOrder notifyAboutOrder;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FoodService foodService;

    @Transactional
    public boolean todayMenuExist() {
        return menuDao.todayMenuExist();
    }

    @Transactional
    public boolean todayMenuIsActive() {
        return menuDao.todayMenuIsActive();
    }

    @Transactional
    public MenuDto getTodayMenu() {
        Menu menu = todayMenu();
        return menu != null ? MenuDto.convertToDto(menu) : null;
    }

    @Transactional
    public List<MenuDto> getTodayMenues() {
        List<Menu> menues = menuDao.getTodayMenues();
        List<MenuDto> menuesDto = new ArrayList<>();
        menues.forEach(menu -> menuesDto.add(MenuDto.convertToDto(menu)));
        return menuesDto;
    }

    @Transactional
    protected Menu todayMenu() {
        return menuDao.getTodayMenu();
    }

    @Transactional
    public void addMenu(MenuDto menuDto) {
        Menu menu = new Menu(new Date(), menuDto.getDateAcceptOrderD(), foodService.find(menuDto.getFoods()));
        try {
            menuDao.save(menu);
            scheduler.schedule(sendOrderInFoodProvider, menuDto.getDateAcceptOrderD());
            scheduler.schedule(notifyAboutOrder, DateBuilder.getTenMinutesBeforeOrder(menuDto.getDateAcceptOrderD()));
            scheduler.schedule(notifyAboutMenu, DateBuilder.addOneMinutes(today()));
            //orderService.createAutoOrder();
            log.info("Task was created. Menu send in " + menuDto.getDateAcceptOrderD().toString());
        } catch (Exception e) {
            log.error("create menu and task send order is failed!", e);
        }
    }

    @Transactional
    public void repeatSendOrders() {
        scheduler.schedule(sendOrderInFoodProvider, today());
        log.info("Task was created. Repeat send order");
    }

    @Transactional
    public void deleteMenu() {
        log.info("start delete menu and orders...");
        List<MenuDto> menuDtos = getTodayMenues();
        menuDtos.forEach(menuDto -> menuDao.deleteTodayMenu(menuDto.getId()));
    }

    @Transactional
    public void updateMenu(Collection<Food> foods, String acceptDate, long id) throws Exception{
        Menu menu = menuDao.find(id);
        menu.setFoods(foods);
        menu.setDateAcceptingOrder(DateConverter.getFormatDatabase().parse(acceptDate));
        menuDao.update(menu);
    }

    @Transactional
    public void deactivateTodayMenu() {
        Menu menu = todayMenu();
        if (menu == null) {
            log.info("Today menu is null");
            return;
        }
        menu.setActive(false);
        menuDao.update(menu);
    }

    @Transactional
    public StatusOrder statusOrder() {
        if (!todayMenuExist())
            return StatusOrder.MENU_NOT_EXIST;
        if (todayMenuIsActive())
            return StatusOrder.MENU_IS_ACTIVE;
        if(orderService.todayOrdersAccept())
            return StatusOrder.ORDER_DELIVERED;
        return StatusOrder.WAITING_DELIVERY;
    }
}
