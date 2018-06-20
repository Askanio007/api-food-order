package controller;

import converter.MoneyToString;
import dto.OrderDto;
import models.filters.ReportFilters;
import models.reports.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.OrderService;
import service.ReportService;
import service.UserService;
import utils.DateFilter;
import utils.PaginationFilter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static controller.UserController.getCurrentUserName;

@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/rest/report/defaultTimePeriod", method = RequestMethod.GET)
    public DateFilter defaultTimePeriod() {
        return DateFilter.currentCashPeriod();
    }

    @RequestMapping(value = "/rest/report/lastTimePeriod", method = RequestMethod.GET)
    public DateFilter lastTimePeriod() {
        return DateFilter.lastCashPeriod();
    }

    // Today orders
    @RequestMapping(value = "/rest/todayOrderActive", method = RequestMethod.GET)
    public boolean todayOrdersActive() {
        return orderService.todayOrdersAccept();
    }

    @RequestMapping(value = "/rest/report/todayOrders", method = RequestMethod.GET)
    public List<ReportByNameFood> todayOrders() {
        return reportService.getTodayReportByTypeFood();
    }

    @RequestMapping(value = "/rest/report/todayOtherOrders", method = RequestMethod.GET)
    public List<ReportByNameFood> todayOtherOrders() {
        return reportService.getTodayReportByTypeOtherFood();
    }

    @RequestMapping(value = "/rest/report/amountSumTodayOrders", method = RequestMethod.GET)
    public String amountSumTodayOrders() {
        return MoneyToString.convert(reportService.getAmountTodayReportByTypeFood());
    }

    @RequestMapping(value = "/rest/report/amountSumTodayOtherOrders", method = RequestMethod.GET)
    public String amountSumTodayOtherOrders() {
        return MoneyToString.convert(reportService.getAmountTodayReportByTypeOtherFood());
    }


    //By Sum Orders
    @RequestMapping(value = "/rest/report/bySumOrder", method = RequestMethod.GET)
    public List<ReportOrderPriceByDate> bySumOrder() {
        return reportService.getOrderPriceByDate(DateFilter.currentCashPeriod(), PaginationFilter.defaultPagination());
    }

    @RequestMapping(value = "/rest/report/countBySumOrder", method = RequestMethod.GET)
    public long countBySumOrder() {
        return reportService.getCountOrderPriceByDate(DateFilter.currentCashPeriod());
    }

    @RequestMapping(value = "/rest/report/bySumOrder", method = RequestMethod.POST)
    public List<ReportOrderPriceByDate> bySumOrder(@RequestBody ReportFilters reportFilters) {
        return reportService.getOrderPriceByDate(DateFilter.generateDateFilter(reportFilters), reportFilters.getPaginationFilter());
    }

    @RequestMapping(value = "/rest/report/countBySumOrder", method = RequestMethod.POST)
    public long countBySumOrder(@RequestBody ReportFilters reportFilters) {
        return reportService.getCountOrderPriceByDate(DateFilter.generateDateFilter(reportFilters));
    }

    @RequestMapping(value = "/rest/report/amountSumOrder", method = RequestMethod.GET)
    public String amountSumOrder() {
        return MoneyToString.convert(reportService.getAmountOrderPriceByDate(DateFilter.currentCashPeriod()));
    }

    @RequestMapping(value = "/rest/report/amountSumOrder", method = RequestMethod.POST)
    public String amountSumOrder(@RequestBody ReportFilters reportFilters) {
        return MoneyToString.convert(reportService.getAmountOrderPriceByDate(DateFilter.generateDateFilter(reportFilters)));
    }


    //Detail By Order

    @RequestMapping(value = "/rest/report/countDetailByOrder", method = RequestMethod.GET)
    public long countDetailByOrder() {
        return orderService.countOrderByDate(DateFilter.currentCashPeriod());
    }

    @RequestMapping(value = "/rest/report/countDetailByOrder", method = RequestMethod.POST)
    public long countDetailByOrder(@RequestBody ReportFilters reportFilters) {
        return orderService.countOrderByDate(DateFilter.generateDateFilter(reportFilters));
    }

    @RequestMapping(value = "/rest/report/detailByOrder", method = RequestMethod.GET)
    public List<OrderDto> DetailByOrder() {
        return orderService.findByDate(DateFilter.currentCashPeriod(), PaginationFilter.defaultPagination());
    }

    @RequestMapping(value = "/rest/report/detailByOrder", method = RequestMethod.POST)
    public List<OrderDto> DetailByOrder(@RequestBody ReportFilters reportFilters) {
        return orderService.findByDate(DateFilter.generateDateFilter(reportFilters), reportFilters.getPaginationFilter());
    }


    //Main page manager

    @RequestMapping(value = "/rest/report/priceByType", method = RequestMethod.GET)
    public List<ReportByTypeFood> priceByTypes() {
        return reportService.getPriceByTypes();
    }

    @RequestMapping(value = "/rest/report/amountSumPriceByType", method = RequestMethod.GET)
    public String amountSumPriceByTypes() {
        return MoneyToString.convert(reportService.getAmountSumPriceByTypes());
    }

    @RequestMapping(value = "/rest/report/currentBySumOrder", method = RequestMethod.GET)
    public List<ReportOrderPriceByDate> currentBySumOrder() {
        return reportService.getOrderPriceByDate(DateFilter.currentCashPeriod(), PaginationFilter.defaultPagination());
    }

    @RequestMapping(value = "/rest/report/amountSumCurrentBySumOrder", method = RequestMethod.GET)
    public String amountSumCurrentBySumOrder() {
        return MoneyToString.convert(reportService.getAmountSumOrderPriceByDate());
    }


    // Main page customer

    @RequestMapping(value = "/rest/report/priceByTypeByUser", method = RequestMethod.GET)
    public List<ReportByTypeFood> priceByTypesForUser() {
        return reportService.getPriceByTypes(getCurrentUserName());
    }

    @RequestMapping(value = "/rest/report/amountSumPriceByTypeByUser", method = RequestMethod.GET)
    public Object amountSumPriceByTypesForUser() {
        return MoneyToString.convert(reportService.amountSumPriceByTypes(getCurrentUserName()));
    }


    // Manager report
    @RequestMapping(value = "/rest/report/customerByMoney", method = RequestMethod.GET)
    public List<ReportUserByMoney> customerByMoney() {
        return userService.customerByMoney(new ReportFilters(PaginationFilter.defaultPagination()));
    }

    @RequestMapping(value = "/rest/report/customerByMoney", method = RequestMethod.POST)
    public List<ReportUserByMoney> customerByMoney(@RequestBody ReportFilters reportFilters) {
        return userService.customerByMoney(reportFilters);
    }

    @RequestMapping(value = "/rest/report/amountSumCustomerByMoney", method = RequestMethod.GET)
    public String amountSumCustomerByMoney() {
        return MoneyToString.convert(userService.amountSumCustomerByMoney(new ReportFilters(PaginationFilter.defaultPagination())));
    }

    @RequestMapping(value = "/rest/report/amountSumCustomerByMoney", method = RequestMethod.POST)
    public String amountSumCustomerByMoney(@RequestBody ReportFilters reportFilters) {
        return MoneyToString.convert(userService.amountSumCustomerByMoney(reportFilters));
    }

    @RequestMapping(value = "/rest/report/todayOrderByName", method = RequestMethod.GET)
    public List<ReportByTodayOrder> todayOrderByName() {
        return reportService.getReportByTodayOrder();
    }


    //User report

    @RequestMapping(value = "/rest/customer/bySumOrderByUser", method = RequestMethod.GET)
    public List<ReportOrderPriceByDate> bySumOrderForUser() {
        return reportService.getOrderPriceByDate(DateFilter.currentCashPeriod(), getCurrentUserName(), PaginationFilter.defaultPagination());
    }

    @RequestMapping(value = "/rest/customer/bySumOrderByUser", method = RequestMethod.POST)
    public List<ReportOrderPriceByDate> bySumOrderForUser(@RequestBody ReportFilters reportFilters) {
        return reportService.getOrderPriceByDate(DateFilter.generateDateFilter(reportFilters), getCurrentUserName(), reportFilters.getPaginationFilter());
    }

    @RequestMapping(value = "/rest/customer/detailByOrderByUser", method = RequestMethod.GET)
    public List<OrderDto> detailByOrderForUser() {
        return orderService.findByDate(DateFilter.currentCashPeriod(), getCurrentUserName(), PaginationFilter.defaultPagination());
    }

    @RequestMapping(value = "/rest/customer/detailByOrderByUser", method = RequestMethod.POST)
    public List<OrderDto> detailByOrderForUser(@RequestBody ReportFilters reportFilters) {
        return orderService.findByDate(DateFilter.generateDateFilter(reportFilters), getCurrentUserName(), reportFilters.getPaginationFilter());
    }

    // detail
    @RequestMapping(value = "/rest/report/detailByDay", method = RequestMethod.POST)
    public List<ReportByNameFood> detailByDay(@RequestBody Date date) throws Exception {
        return reportService.getReportByNameFood(date);
    }

    @RequestMapping(value = "/rest/report/otherDetailByDay", method = RequestMethod.POST)
    public List<ReportByNameFood> detailOtherByDay(@RequestBody Date date) {
        return reportService.getReportOtherByNameFood(date);
    }

    @RequestMapping(value = "/rest/report/sumDetailByDay", method = RequestMethod.POST)
    public BigDecimal sumDetailByDay(@RequestBody Date date) {
        return reportService.getAmountReportByNameFood(date);
    }

    @RequestMapping(value = "/rest/report/sumDetailOtherByDay", method = RequestMethod.POST)
    public BigDecimal sumDetailOtherByDay(@RequestBody Date date) {
        return reportService.getAmountReportByNameOtherFood(date);
    }

}
