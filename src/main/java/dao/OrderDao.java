package dao;

import dto.FoodTypeDto;
import entity.Order;
import models.*;
import models.reports.ReportByNameFood;
import models.reports.ReportByTypeFood;
import models.reports.ReportOrderPriceByDate;
import utils.DateFilter;
import utils.PaginationFilter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderDao extends GenericDao<Order> {

    Order findToday(String name);

    void deleteTodayOrders(long id);

    List<Order> findToday();

    List<Order> findAllOrder();

    BigDecimal sumTodayOrder(String name);

    List<ReportByNameFood> getTodayReportByNameFood(List<FoodTypeDto> listType);

    List<Product> getFullTodayOrder();

    long countTodayOrders();

    boolean todayOrdersAccept();

    boolean todayOrdersAccept(Date dateAcceptingOrder);

    BigDecimal amountMoneySumOrders(String login, Date from, Date to);

    BigDecimal amountMoneyTodayOrders(List<FoodTypeDto> listType);

    List<Order> orderByFood(long id);

}
