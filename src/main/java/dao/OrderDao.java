package dao;

import entity.Order;
import models.*;
import models.reports.ReportByNameFood;
import models.reports.ReportByTypeFood;
import models.reports.ReportOrderPriceByDate;
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

    List<Order> findByDate(Date from, Date to, String login, PaginationFilter paginationFilter);

    long countOrderByDate(Date from, Date to, String login);

    List<ReportByTypeFood> getReportByTypeFood(String userLogin);

    BigDecimal amountSumByTypeFood(String userLogin);

    List<ReportByNameFood> getTodayReportByNameFood();

    List<ReportByNameFood> getReportByNameFood(Date date);

    List<Product> getFullTodayOrder();

    long countTodayOrders();

    boolean todayOrdersAccept();

    boolean todayOrdersAccept(Date dateAcceptingOrder);

    List<ReportByNameFood> getTodayReportByTypeOtherFood();

    List<ReportByNameFood> getReportByTypeOtherFood(Date date);

    List<ReportOrderPriceByDate> getOrderPriceByDate(String userLogin, Date from, Date to, PaginationFilter paginationFilter);

    long getCountOrderPriceByDate(String userLogin, Date from, Date to);

    BigDecimal amountMoneySumOrders(Date from, Date to);

    BigDecimal amountMoneySumOrders(String login, Date from, Date to);

    BigDecimal amountMoneyTodayOrders();

    BigDecimal amountMoneyTodayOtherOrders();

    BigDecimal amountMoneyOrders(Date date);

    BigDecimal amountMoneyOtherOrders(Date date);

    BigDecimal amountSumReportByTypeFood();

    BigDecimal amountSumOrderPriceByDate();

    List<Order> orderByFood(long id);

}
