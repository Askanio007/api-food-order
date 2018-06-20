package service;

import dao.OrderDao;
import models.reports.ReportByNameFood;
import models.reports.ReportByTodayOrder;
import models.reports.ReportByTypeFood;
import models.reports.ReportOrderPriceByDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import utils.DateFilter;
import utils.PaginationFilter;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    @Qualifier("orderDao")
    private OrderDao orderDao;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Transactional
    public List<ReportByTypeFood> getPriceByTypes() {
        return orderDao.getReportByTypeFood(null);
    }

    @Transactional
    public BigDecimal getAmountSumPriceByTypes() {
        return orderDao.amountSumReportByTypeFood();
    }

    @Transactional
    public List<ReportByTypeFood> getPriceByTypes(String userLogin) {
        return orderDao.getReportByTypeFood(userLogin);
    }

    @Transactional
    public BigDecimal amountSumPriceByTypes(String userLogin) {
        return orderDao.amountSumByTypeFood(userLogin);
    }

    @Transactional
    public BigDecimal getAmountSumOrderPriceByDate() {
        return orderDao.amountSumOrderPriceByDate();
    }


    @Transactional
    public List<ReportOrderPriceByDate> getOrderPriceByDate(DateFilter dateFilter, PaginationFilter paginationFilter) {
        return orderDao.getOrderPriceByDate(null, dateFilter.getFrom(), dateFilter.getTo(), paginationFilter);
    }

    @Transactional
    public List<ReportOrderPriceByDate> getOrderPriceByDate(DateFilter dateFilter, String login, PaginationFilter paginationFilter) {
        return orderDao.getOrderPriceByDate(login, dateFilter.getFrom(), dateFilter.getTo(), paginationFilter);
    }

    @Transactional
    public long getCountOrderPriceByDate(DateFilter dateFilter) {
        return orderDao.getCountOrderPriceByDate(null, dateFilter.getFrom(), dateFilter.getTo());
    }

    @Transactional
    public long getCountOrderPriceByDate(DateFilter dateFilter, String login) {
        return orderDao.getCountOrderPriceByDate(login, dateFilter.getFrom(), dateFilter.getTo());
    }

    @Transactional
    public List<ReportByNameFood> getTodayReportByTypeFood() {
        return orderDao.getTodayReportByNameFood();
    }

    @Transactional
    public List<ReportByNameFood> getTodayReportByTypeOtherFood() {
        return orderDao.getTodayReportByTypeOtherFood();
    }

    @Transactional
    public List<ReportByTodayOrder> getReportByTodayOrder() {
        return userService.getReportByTodayOrder();
    }

    @Transactional
    public BigDecimal getAmountOrderPriceByDate(DateFilter filter) {
        return orderDao.amountMoneySumOrders(filter.getFrom(), filter.getTo());
    }

    @Transactional
    public BigDecimal getAmountTodayReportByTypeFood() {
        return orderDao.amountMoneyTodayOrders();
    }

    @Transactional
    public BigDecimal getAmountTodayReportByTypeOtherFood() {
        return orderDao.amountMoneyTodayOtherOrders();
    }


    @Transactional
    public List<ReportByNameFood> getReportByNameFood(Date date) {
        return orderDao.getReportByNameFood(date);
    }

    @Transactional
    public List<ReportByNameFood> getReportOtherByNameFood(Date date) {
        return orderDao.getReportByTypeOtherFood(date);
    }

    @Transactional
    public BigDecimal getAmountReportByNameFood(Date date) {
        return orderDao.amountMoneyOrders(date);
    }

    @Transactional
    public BigDecimal getAmountReportByNameOtherFood(Date date) {
        return orderDao.amountMoneyOtherOrders(date);
    }

}
