package service;

import dao.CompletedOrderDao;
import dao.OrderDao;
import dto.AutoOrderDto;
import dto.FoodDto;
import dto.MenuDto;
import dto.OrderDto;
import entity.*;
import enums.FoodType;
import models.Product;
import models.xmlOrder.Address;
import models.xmlOrder.Phone;
import models.xmlOrder.Products;
import models.xmlOrder.XMLOrder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import utils.DateFilter;
import utils.PaginationFilter;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

import static utils.DateBuilder.today;

@Service
public class OrderService {

    private static final Logger log = Logger.getLogger(OrderService.class);

    @Autowired
    @Qualifier("orderDao")
    private OrderDao orderDao;

    @Autowired
    @Qualifier("completedOrder")
    private CompletedOrderDao completedOrderDao;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private TransactionService transactionService;

    private BigDecimal calculatePrice(Collection<Food> foods) {
        return foods.stream().map(Food::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void save(List<FoodDto> foodDto, long userId) {
        save(foodService.find(foodDto), userService.get(userId));
    }

    @Transactional
    public void save(List<FoodDto> foodDto, String userName) {
        save(foodService.find(foodDto), userService.get(userName));
    }

    private void save(List<Food> foods, User user) {
        Order order = new Order(user, foods, calculatePrice(foods));
        orderDao.save(order);
    }

    @Transactional
    public void update(List<FoodDto> foods, String login) {
        update(foodService.find(foods), login);
    }

    @Transactional
    protected void update(Collection<Food> foods, String login) {
        Order order = orderDao.findToday(login);
        order.setPrice(calculatePrice(foods));
        order.setDateOrder(new Date());
        order.setFoods(foods);
        orderDao.update(order);
    }

    @Transactional
    public boolean todayOrdersAccept() {
        Menu menu = menuService.todayMenu();
        if (menu != null)
            if (menu.getDateAcceptingOrder() != null)
                return orderDao.todayOrdersAccept(menu.getDateAcceptingOrder());
        return orderDao.todayOrdersAccept();
    }

    @Transactional
    public void acceptingTodayOrder() {
        try {
            BigDecimal sumMenuFood = orderDao.amountMoneyTodayOrders();
            BigDecimal sumOtherFood = orderDao.amountMoneyTodayOtherOrders();
            BigDecimal sumOrder = sumMenuFood.add(sumOtherFood);
            List<Order> orders = findToday();
            orders.forEach(order -> {
                order.setAccept(true);
                order.setDateDeliveredOrder(today());
                CompletedOrder completedOrder = new CompletedOrder(order);
                completedOrder.setItems(CompletedOrderItem.createItem(order.getFoods(), completedOrder));
                completedOrderDao.save(completedOrder);
                orderDao.update(order);
            });

            transactionService.paymentTransaction(sumOrder);
        } catch (Exception e) {
            log.error("update orders or update balance is failed", e);
        }
    }

    @Transactional
    protected BigDecimal sumTodayOrder() {
        BigDecimal sumMenuFood = orderDao.amountMoneyTodayOrders();
        BigDecimal sumOtherFood = orderDao.amountMoneyTodayOtherOrders();
        BigDecimal sumOrder = sumMenuFood.add(sumOtherFood);
        return sumOrder;
    }

    @Transactional
    public boolean haveEnoughMoney() {
        BigDecimal cash = userService.currentBalance();
        BigDecimal sumOrder = sumTodayOrder();
        int i = cash.compareTo(sumOrder);
        return i >= 0;
    }

    @Transactional
    public Products getXMLTodayProducts() {
        List<Product> products = orderDao.getFullTodayOrder();
        return new Products(products);

    }

    @Transactional
    public long countTodayOrder() {
        return orderDao.countTodayOrders();
    }

    @Transactional
    public List<OrderDto> findByDate(DateFilter filter, PaginationFilter paginationFilter) {
        return findByDate(filter, null, paginationFilter);
    }

    @Transactional
    public List<OrderDto> findByDate(DateFilter filter, String login, PaginationFilter paginationFilter) {
        List<Order> orders = orderDao.findByDate(filter.getFrom(), filter.getTo(), login, paginationFilter);
        return orders != null ? OrderDto.convertToDto(orders) : null;
    }

    @Transactional
    public long countOrderByDate(DateFilter filter, String login) {
        return orderDao.countOrderByDate(filter.getFrom(), filter.getTo(), login);
    }

    @Transactional
    public long countOrderByDate(DateFilter filter) {
        return countOrderByDate(filter, null);
    }

    @Transactional
    public BigDecimal lastPeriodBalance(String userLogin) {
        DateFilter dateFilter = DateFilter.lastCashPeriod();
        return orderDao.amountMoneySumOrders(userLogin, dateFilter.getFrom(), dateFilter.getTo());
    }

    @Transactional
    public List<Order> findToday() {
        return orderDao.findToday();
    }

    @Transactional
    public void deleteTodayOrders() {
        List<Order> today = orderDao.findAllOrder();
        today.forEach(order -> orderDao.deleteTodayOrders(order.getId()));
        log.info("delete finished!");
    }

    @Transactional
    public OrderDto findToday(String name) {
        return OrderDto.convertToDto(orderDao.findToday(name));
    }

    @Transactional
    public BigDecimal sumTodayOrder(String name) {
        return orderDao.sumTodayOrder(name);
    }

    public void createAutoOrder() {
        List<AutoOrderDto> autoOrders = userService.listAutoOrder();
        MenuDto menu = menuService.getTodayMenu();
        for (AutoOrderDto autoOrderDto : autoOrders) {
            List<Food> foods = new ArrayList<>();
            if (autoOrderDto.getType() == null)
                foods = foodService.find(extractFood(menu.getFoods(), autoOrderDto.getFoods()));
            else {
                switch (autoOrderDto.getType()) {
                    case "LIGHT": {
                        foods = foodService.find(createFoodList(FoodType.lightBusinessLunch(), menu.getFoods()));
                        break;
                    }
                    case "MEDIUM": {
                        foods = foodService.find(createFoodList(FoodType.mediumBusinessLunch(), menu.getFoods()));
                        break;
                    }
                    case "FULL": {
                        foods = foodService.find(createFoodList(FoodType.fullBusinessLunch(), menu.getFoods()));
                        break;
                    }
                }
                foods.addAll(foodService.find(extractFood(menu.getFoods(), autoOrderDto.getFoods())));
            }
            if (!foods.isEmpty())
                save(foods, userService.get(autoOrderDto.getIdUser()));
        }
    }

    private List<FoodDto> createFoodList(List<FoodType> types, List<FoodDto> menuFoods) {
        List<FoodDto> foods = new ArrayList<>();
        types.forEach(type -> foods.addAll(extractFood(menuFoods, type)));
        return foods;
    }

    private List<FoodDto> extractFood(List<FoodDto> foods, FoodType type) {
        List<FoodDto> foodDto = new ArrayList<>();
        Optional foodOptional = foods.stream().filter(f -> f.getType().equals(FoodType.getType(type))).findFirst();
        if (foodOptional.isPresent())
            foodDto.add((FoodDto) foodOptional.get());
        return foodDto;
    }

    private List<FoodDto> extractFood(List<FoodDto> menuFoods, List<FoodDto> autoOrderFoods) {
        List<FoodDto> l = new ArrayList<>();
        menuFoods.forEach(menuF -> {
            autoOrderFoods.forEach(ao -> {
                if (ao.getId() == menuF.getId())
                    l.add(ao);
            });
        });
        return l;
    }

    @Transactional
    public void deleteOrder(String login) {
        Order order = orderDao.findToday(login);
        order.getFoods().forEach(foodDto -> {
            Food food = foodService.find(foodDto.getId());
            food.getOrders().remove(order);
            foodService.updateFood(food);
        });
        order.getFoods().clear();
        orderDao.delete(order.getId());
    }

    @Transactional
    public XMLOrder generateXmlOrder() {
        XMLOrder order = new XMLOrder(countTodayOrder(), getXMLTodayProducts());
        order.setAddress(new Address(settingService.getCity(), settingService.getStreet(), settingService.getHouse(), settingService.getFlat(), settingService.getFloor()));
        order.setPhone(new Phone(settingService.getPhone()));
        return order;
    }

    @Transactional
    public void deleteFoodFromOrder(Food food) {
        List<Order> orders = orderDao.orderByFood(food.getId());
        orders.forEach(order -> {
            order.getFoods().remove(food);
            if (order.getFoods().isEmpty())
                orderDao.delete(order.getId());
            else
                orderDao.update(order);
        });
    }
}
