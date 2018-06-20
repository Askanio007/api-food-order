package controller.v2;

import converter.MoneyToString;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.ReportService;

@RestController
@RequestMapping("/api/v2/report/today")
@Api(value = "/api/v2/report/today", description = "Отчёты на текущий день")
public class TodayReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "Текущий заказ из бизнес меню")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/business", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> todayOrders() {
        return ResponseServer.OK(true, reportService.getTodayReportByTypeFood());
    }

    @ApiOperation(value = "Текущий заказ из общего меню")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/other", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> todayOtherOrders() {
        return ResponseServer.OK(true, reportService.getTodayReportByTypeOtherFood());
    }

    @ApiOperation(value = "Сумма заказа из бизнес меню")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/business/sum", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> amountSumTodayOrders() {
        return ResponseServer.OK(true, MoneyToString.convert(reportService.getAmountTodayReportByTypeFood()));
    }

    @ApiOperation(value = "Сумма заказа из общего меню")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/other/sum", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> amountSumTodayOtherOrders() {
        return ResponseServer.OK(true, MoneyToString.convert(reportService.getAmountTodayReportByTypeOtherFood()));
    }

    @ApiOperation(value = "Список едоков по имени и сегодняшнему заказу")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/rest/report/todayOrderByName", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> todayOrderByName() {
        return ResponseServer.OK(true, reportService.getReportByTodayOrder());
    }
}
