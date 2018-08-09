package controller;

import dto.TransactionDto;
import models.filters.ReportFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.ReportService;
import service.TransactionService;
import utils.DateFilter;
import utils.PaginationFilter;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/rest/transaction/lastTransaction", method = RequestMethod.GET)
    public List<TransactionDto> lastTransaction() {
        return transactionService.findLast();
    }

    @RequestMapping(value = "/rest/transaction/list", method = RequestMethod.POST)
    public List<TransactionDto> transaction(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return transactionService.find(reportService.defaultCashPeriod(), PaginationFilter.defaultPagination());
        return transactionService.find(DateFilter.generateDateFilter(reportFilters), reportFilters.getPaginationFilter());
    }

    @RequestMapping(value = "/rest/transaction/count", method = RequestMethod.POST)
    public long countTransaction(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return transactionService.countTransactions(reportService.defaultCashPeriod());
        return transactionService.countTransactions(DateFilter.generateDateFilter(reportFilters));
    }

    @RequestMapping(value = "/rest/transaction/add", method = RequestMethod.POST)
    public ResponseEntity<String> addTransaction(@RequestBody @Valid TransactionDto transaction, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>("Not correct sum or empty comment", HttpStatus.BAD_REQUEST);
        transactionService.createTransactionTo(transaction.getSum(), transaction.getComment());
        return new ResponseEntity<>("created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/transaction/complete/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> completedTransaction(@PathVariable("id") long id) {
        transactionService.completeTransaction(id);
        return new ResponseEntity<>("completed", HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/transaction/sumTransaction", method = RequestMethod.POST)
    public BigDecimal currentBalance(@RequestBody(required = false) ReportFilters reportFilters) {
        if (reportFilters == null)
            return transactionService.sumTransaction(reportService.defaultCashPeriod());
        return transactionService.sumTransaction(DateFilter.generateDateFilter(reportFilters));
    }
}
