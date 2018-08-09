package controller;

import converter.MoneyToString;
import dto.CompletedOrderDto;
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
        return reportService.defaultCashPeriod();
    }

    @RequestMapping(value = "/rest/report/lastTimePeriod", method = RequestMethod.GET)
    public DateFilter lastTimePeriod() {
        return reportService.lastCashPeriod();
    }

    @RequestMapping(value = "/rest/report/prevCashPeriod", method = RequestMethod.POST)
    public DateFilter prevCashPeriod(@RequestBody Date date) {
        return reportService.getPrevCashPeriod(date);
    }

    @RequestMapping(value = "/rest/report/nextCashPeriod", method = RequestMethod.POST)
    public DateFilter nextCashPeriod(@RequestBody Date date) {
        return reportService.getNextCashPeriod(date);
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

    @RequestMapping(value = "/rest/report/bySumOrder", method = RequestMethod.POST)
    public List<ReportOrderPriceByDate> bySumOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return reportService.getOrderPriceByDate(reportService.defaultCashPeriod(), PaginationFilter.defaultPagination());
        return reportService.getOrderPriceByDate(DateFilter.generateDateFilter(reportFilters), reportFilters.getPaginationFilter());
    }

    @RequestMapping(value = "/rest/report/countBySumOrder", method = RequestMethod.POST)
    public long countBySumOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return reportService.getCountOrderPriceByDate(reportService.defaultCashPeriod());
        return reportService.getCountOrderPriceByDate(DateFilter.generateDateFilter(reportFilters));
    }

    @RequestMapping(value = "/rest/report/amountSumOrder", method = RequestMethod.POST)
    public String amountSumOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return MoneyToString.convert(reportService.getAmountOrderPriceByDate(reportService.defaultCashPeriod()));
        return MoneyToString.convert(reportService.getAmountOrderPriceByDate(DateFilter.generateDateFilter(reportFilters)));
    }


    //Detail By Order

    @RequestMapping(value = "/rest/report/countDetailByOrder", method = RequestMethod.POST)
    public long countDetailByOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return reportService.countOrderByDate(reportService.defaultCashPeriod());
        return reportService.countOrderByDate(DateFilter.generateDateFilter(reportFilters));
    }

    @RequestMapping(value = "/rest/report/detailByOrder", method = RequestMethod.POST)
    public List<CompletedOrderDto> DetailByOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return reportService.findByDate(reportService.defaultCashPeriod(), PaginationFilter.defaultPagination());
        return reportService.findByDate(DateFilter.generateDateFilter(reportFilters), reportFilters.getPaginationFilter());
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
        return reportService.getOrderPriceByDate(reportService.defaultCashPeriod(), PaginationFilter.defaultPagination());
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

    @RequestMapping(value = "/rest/report/customerByMoney", method = RequestMethod.POST)
    public List<ReportUserByMoney> customerByMoney(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return userService.customerByMoney(new ReportFilters(PaginationFilter.defaultPagination()));
        return userService.customerByMoney(reportFilters);
    }

    @RequestMapping(value = "/rest/report/amountSumCustomerByMoney", method = RequestMethod.POST)
    public String amountSumCustomerByMoney(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return MoneyToString.convert(userService.amountSumCustomerByMoney(new ReportFilters(PaginationFilter.defaultPagination())));
        return MoneyToString.convert(userService.amountSumCustomerByMoney(reportFilters));
    }

    @RequestMapping(value = "/rest/report/todayOrderByName", method = RequestMethod.GET)
    public List<ReportByTodayOrder> todayOrderByName() {
        return reportService.getReportByTodayOrder();
    }


    // detail
    @RequestMapping(value = "/rest/report/detailByDay", method = RequestMethod.POST)
    public List<ReportByNameFood> detailByDay(@RequestBody Date date) {
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
