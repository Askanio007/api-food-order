package controller.v2;

import converter.MoneyToString;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import models.filters.ReportFilters;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.OrderService;
import service.ReportService;
import utils.DateFilter;
import utils.PaginationFilter;

import java.util.Date;

@RestController
@RequestMapping("/api/v2/report")
@Api(value = "/api/v2/report", description = "Общие отчёты для распорядителя и гастронома")
public class GeneralReportController {

    @Autowired
    private ReportService reportService;


    @ApiOperation(value = "Даты прошлого отчётного периода (относительно заданного месяца)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/prevCashPeriod", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> prevCashPeriod(@RequestBody Date date) {
        return ResponseServer.OK(true, reportService.getPrevCashPeriod(date));
    }

    @ApiOperation(value = "Даты следующего отчётного периода (относительно заданного месяца)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/nextCashPeriod", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> nextCashPeriod(@RequestBody Date date) {
        return ResponseServer.OK(true, reportService.getNextCashPeriod(date));
    }


    @ApiOperation(value = "Даты текущего отчётного периода")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/defaultTimePeriod", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> defaultTimePeriod() {
        return ResponseServer.OK(true, reportService.defaultCashPeriod());
    }

    @ApiOperation(value = "Даты прошлого отчётного периода")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/lastTimePeriod", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> lastTimePeriod() {
        return ResponseServer.OK(true, reportService.lastCashPeriod());
    }


    //By Sum Orders

    @ApiOperation(value = "Сумма заказов по дням с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/bySumOrders", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> bySumOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, reportService.getOrderPriceByDate(reportService.defaultCashPeriod(), PaginationFilter.defaultPagination()));
        return ResponseServer.OK(true, reportService.getOrderPriceByDate(DateFilter.generateDateFilter(reportFilters), reportFilters.getPaginationFilter()));
    }

    @ApiOperation(value = "Сумма заказов по дням с учётом фильтров. Количество записей")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/bySumOrders/count", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> countBySumOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, reportService.getCountOrderPriceByDate(reportService.defaultCashPeriod()));
        return ResponseServer.OK(true, reportService.getCountOrderPriceByDate(DateFilter.generateDateFilter(reportFilters)));
    }

    @ApiOperation(value = "Сумма заказов по дням с учётом фильтров. Итоговая сумма")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/bySumOrders/sum", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> amountSumOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, MoneyToString.convert(reportService.getAmountOrderPriceByDate(reportService.defaultCashPeriod())));
        return ResponseServer.OK(true, MoneyToString.convert(reportService.getAmountOrderPriceByDate(DateFilter.generateDateFilter(reportFilters))));
    }

    @ApiOperation(value = "Детальная информация по заказу. Блюда из бизнес меню")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/bySumOrders/businessDetail", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> detailByDay(@RequestBody Date date) throws Exception {
        return ResponseServer.OK(true, reportService.getReportByNameFood(date));
    }

    @ApiOperation(value = "Детальная информация по заказу. Блюда из общего меню")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/bySumOrders/otherDetail", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> detailOtherByDay(@RequestBody Date date) {
        return ResponseServer.OK(true, reportService.getReportOtherByNameFood(date));
    }

    @ApiOperation(value = "Детальная информация по заказу. Сумма заказа из бизнес меню")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/bySumOrders/businessDetail/sum", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> sumDetailByDay(@RequestBody Date date) {
        return ResponseServer.OK(true, reportService.getAmountReportByNameFood(date));
    }

    @ApiOperation(value = "Детальная информация по заказу. Сумма заказа из общего меню")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/bySumOrders/otherDetail/sum", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> sumDetailOtherByDay(@RequestBody Date date) {
        return ResponseServer.OK(true, reportService.getAmountReportByNameOtherFood(date));
    }


    //Detail By Order

    @ApiOperation(value = "Количество заказов с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/orders/count", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> countDetailByOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, reportService.countOrderByDate(reportService.defaultCashPeriod()));
        return ResponseServer.OK(true, reportService.countOrderByDate(DateFilter.generateDateFilter(reportFilters)));
    }

    @ApiOperation(value = "Список заказов с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> DetailByOrder(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, reportService.findByDate(reportService.defaultCashPeriod(), PaginationFilter.defaultPagination()));
        return ResponseServer.OK(true, reportService.findByDate(DateFilter.generateDateFilter(reportFilters), reportFilters.getPaginationFilter()));
    }

}
