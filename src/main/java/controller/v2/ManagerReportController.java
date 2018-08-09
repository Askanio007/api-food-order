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
import service.ReportService;
import service.UserService;
import utils.DateFilter;
import utils.PaginationFilter;

@RestController
@RequestMapping("/api/v2/manager/report")
@Api(value = "/api/v2/manager/report", description = "Отчёты распорядителя")
public class ManagerReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;


    // Manager report

    @ApiOperation(value = "Едоки по сумме потраченных денег с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/customerByMoney", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> customerByMoney(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, userService.customerByMoney(new ReportFilters(PaginationFilter.defaultPagination())));
        return ResponseServer.OK(true, userService.customerByMoney(reportFilters));
    }

    @ApiOperation(value = "Едоки по сумме потраченных денег с учётом фильтров. Итоговая сумма")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/customerByMoney/sum", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> amountSumCustomerByMoney(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, MoneyToString.convert(userService.amountSumCustomerByMoney(new ReportFilters(PaginationFilter.defaultPagination()))));
        return ResponseServer.OK(true, MoneyToString.convert(userService.amountSumCustomerByMoney(reportFilters)));
    }

    //Main page manager

    @ApiOperation(value = "Сумма расходов по типу блюда за текущий отчётный период")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/priceByType", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> priceByTypes() {
        return ResponseServer.OK(true, reportService.getPriceByTypes());
    }

    @ApiOperation(value = "Сумма расходов по типу блюда за текущий отчётный период. Итоговая сумма")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/priceByType/sum", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> amountSumPriceByTypes() {
        return ResponseServer.OK(true, MoneyToString.convert(reportService.getAmountSumPriceByTypes()));
    }

    @ApiOperation(value = "Сумма затрат по каждому дню за текущий период")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/currentBySumOrder", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> currentBySumOrder() {
        return ResponseServer.OK(true, reportService.getOrderPriceByDate(reportService.defaultCashPeriod(), PaginationFilter.defaultPagination()));
    }

    @ApiOperation(value = "Сумма затрат по каждому дню за текущий период. Итоговая сумма")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/currentBySumOrder/sum", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> amountSumCurrentBySumOrder() {
        return ResponseServer.OK(true, MoneyToString.convert(reportService.getAmountSumOrderPriceByDate()));
    }



}
