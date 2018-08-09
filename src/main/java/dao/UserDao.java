package dao;


import entity.AutoOrder;
import entity.Role;
import entity.User;
import models.reports.ReportByTodayOrder;
import models.reports.ReportUserByMoney;
import utils.PaginationFilter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface UserDao extends GenericDao<User> {

    User find(String name);

    List<ReportUserByMoney> find(Date from, Date to, PaginationFilter paginationFilter, String name);

    BigDecimal amountSumCustomerByMoney(Date from, Date to, String name);

    List<ReportByTodayOrder> findTodayOrder(Date dateAcceptMenu);

    List<AutoOrder> findAutoOrders();

    AutoOrder findAutoOrder(String name);

    boolean isActive(String login);

    List<User> find(PaginationFilter paginationFilter, long roleId, String login);

    long countAllCustomer(String name);

    long countAll(String login, long roleId);

    void updateUserBalance(BigDecimal balance, Role role);
}
