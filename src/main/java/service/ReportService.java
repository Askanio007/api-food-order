package service;

import dao.CompletedOrderDao;
import dao.OrderDao;
import dto.CompletedOrderDto;
import entity.CompletedOrder;
import models.reports.ReportByNameFood;
import models.reports.ReportByTodayOrder;
import models.reports.ReportByTypeFood;
import models.reports.ReportOrderPriceByDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import utils.DateBuilder;
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
    @Qualifier("completedOrder")
    private CompletedOrderDao completedOrderDao;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodTypeService foodTypeService;

    @Autowired
    private SettingService settingService;

    @Transactional
    public List<ReportByTypeFood> getPriceByTypes() {
        return completedOrderDao.getReportByTypeFood(null, defaultCashPeriod());
    }

    @Transactional
    public BigDecimal getAmountSumPriceByTypes() {
        return completedOrderDao.amountSumReportByTypeFood(defaultCashPeriod());
    }

    @Transactional
    public List<ReportByTypeFood> getPriceByTypes(String userLogin) {
        return completedOrderDao.getReportByTypeFood(userLogin, defaultCashPeriod());
    }

    @Transactional
    public BigDecimal amountSumPriceByTypes(String userLogin) {
        return completedOrderDao.amountSumByTypeFood(userLogin, defaultCashPeriod());
    }

    @Transactional
    public BigDecimal getAmountSumOrderPriceByDate() {
        return completedOrderDao.amountSumReportByTypeFood(defaultCashPeriod());
    }


    @Transactional
    public List<ReportOrderPriceByDate> getOrderPriceByDate(DateFilter dateFilter, PaginationFilter paginationFilter) {
        return completedOrderDao.getOrderPriceByDate(dateFilter.getFrom(), dateFilter.getTo(), paginationFilter);
    }

    @Transactional
    public long getCountOrderPriceByDate(DateFilter dateFilter) {
        return completedOrderDao.getCountOrderPriceByDate(dateFilter.getFrom(), dateFilter.getTo());
    }

    @Transactional
    public List<ReportByNameFood> getTodayReportByTypeFood() {
        return orderDao.getTodayReportByNameFood(foodTypeService.listComboTypes());
    }

    @Transactional
    public List<ReportByNameFood> getTodayReportByTypeOtherFood() {
        return orderDao.getTodayReportByNameFood(foodTypeService.listPermanentTypes());
    }

    @Transactional
    public List<ReportByTodayOrder> getReportByTodayOrder() {
        return userService.getReportByTodayOrder();
    }

    @Transactional
    public BigDecimal getAmountOrderPriceByDate(DateFilter filter) {
        return completedOrderDao.amountMoneySumOrders(filter.getFrom(), filter.getTo());
    }

    @Transactional
    public BigDecimal getAmountTodayReportByTypeFood() {
        return orderDao.amountMoneyTodayOrders(foodTypeService.listComboTypes());
    }

    @Transactional
    public BigDecimal getAmountTodayReportByTypeOtherFood() {
        return orderDao.amountMoneyTodayOrders(foodTypeService.listPermanentTypes());
    }


    @Transactional
    public List<ReportByNameFood> getReportByNameFood(Date date) {
        return completedOrderDao.getReportByNameFood(date, foodTypeService.listComboTypes());
    }

    @Transactional
    public List<ReportByNameFood> getReportOtherByNameFood(Date date) {
        return completedOrderDao.getReportByNameFood(date, foodTypeService.listPermanentTypes());
    }

    @Transactional
    public BigDecimal getAmountReportByNameFood(Date date) {
        return completedOrderDao.amountMoneyOrders(date, foodTypeService.listComboTypes());
    }

    @Transactional
    public BigDecimal getAmountReportByNameOtherFood(Date date) {
        return completedOrderDao.amountMoneyOrders(date, foodTypeService.listPermanentTypes());
    }

    @Transactional
    public List<CompletedOrderDto> findByDate(DateFilter filter, PaginationFilter paginationFilter) {
        List<CompletedOrder> orders = completedOrderDao.findByDate(filter.getFrom(), filter.getTo(), paginationFilter);
        return CompletedOrderDto.convertToDto(orders);
    }

    @Transactional
    public long countOrderByDate(DateFilter filter) {
        return completedOrderDao.countOrderByDate(filter.getFrom(), filter.getTo());
    }

    public DateFilter defaultCashPeriod() {
        return DateFilter.currentCashPeriod(settingService.getStartAccountingPeriod());
    }

    public DateFilter lastCashPeriod() {
        return DateFilter.lastCashPeriod(settingService.getStartAccountingPeriod());
    }

    @Transactional
    public DateFilter getNextCashPeriod(Date date) {
        return DateBuilder.nextCashPeriod(settingService.getStartAccountingPeriod(), DateBuilder.getCalendar(date));
    }

    @Transactional
    public DateFilter getPrevCashPeriod(Date date) {
        return DateBuilder.prevCashPeriod(settingService.getStartAccountingPeriod(), DateBuilder.getCalendar(date));
    }

}
