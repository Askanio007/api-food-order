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

import static controller.v2.UserControllerV2.getCurrentUserLogin;

@RestController
@RequestMapping("/api/v2/customer/report")
@Api(value = "/api/v2/customer/report", description = "Отчёты едока")
public class CustomerReportController {

    @Autowired
    private ReportService reportService;

    // Main page

    @ApiOperation(value = "Сумма расходов по типу блюда за текущий отчётный период")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/priceByType", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> priceByTypesForUser() {
        return ResponseServer.OK(true, reportService.getPriceByTypes(getCurrentUserLogin()));
    }

    @ApiOperation(value = "Сумма расходов по типу блюда за текущий отчётный период. Итоговая сумма")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/priceByType/sum", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> amountSumPriceByTypesForUser() {
        return ResponseServer.OK(true, MoneyToString.convert(reportService.amountSumPriceByTypes(getCurrentUserLogin())));
    }

}
