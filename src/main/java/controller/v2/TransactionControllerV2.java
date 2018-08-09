package controller.v2;

import dto.TransactionDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import models.filters.ReportFilters;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.ReportService;
import service.TransactionService;
import utils.DateFilter;
import utils.PaginationFilter;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/transaction")
@Api(value = "/api/v2/transaction", description = "Операции с транзакциями")
public class TransactionControllerV2 {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "Последние 10 транзакций")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/lastTen", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> lastTenTransaction() {
        return ResponseServer.OK(true, transactionService.findLast());
    }

    @ApiOperation(value = "Список с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> transaction(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, transactionService.find(reportService.defaultCashPeriod(), PaginationFilter.defaultPagination()));
        return ResponseServer.OK(true, transactionService.find(DateFilter.generateDateFilter(reportFilters), reportFilters.getPaginationFilter()));
    }

    @ApiOperation(value = "Количество с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> countTransaction(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, transactionService.countTransactions(reportService.defaultCashPeriod()));
        return ResponseServer.OK(true, transactionService.countTransactions(DateFilter.generateDateFilter(reportFilters)));
    }

    @ApiOperation(value = "Добавить")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> addTransaction(@RequestBody @Valid TransactionDto transaction, BindingResult result) {
        if (result.hasErrors())
            return ResponseServer.OK(false, "Not correct sum or empty comment");
        transactionService.createTransactionTo(transaction.getSum(), transaction.getComment());
        return ResponseServer.OK(true, "created");
    }

    @ApiOperation(value = "Завершить")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/complete/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> completedTransaction(@PathVariable("id") long id) {
        transactionService.completeTransaction(id);
        return ResponseServer.OK(true, "completed");
    }

    @ApiOperation(value = "Общая сумма с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/sum", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> currentBalance(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return ResponseServer.OK(true, transactionService.sumTransaction(reportService.defaultCashPeriod()));
        return ResponseServer.OK(true, transactionService.sumTransaction(DateFilter.generateDateFilter(reportFilters)));
    }
}