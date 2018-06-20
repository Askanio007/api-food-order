package controller;

import dto.TransactionDto;
import models.filters.ReportFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping(value = "/rest/transaction/lastTransaction", method = RequestMethod.GET)
    public List<TransactionDto> lastTransaction() {
        return transactionService.findLast();
    }

    @RequestMapping(value = "/rest/transaction/list", method = RequestMethod.GET)
    public List<TransactionDto> transaction() {
        return transactionService.find(DateFilter.currentCashPeriod(), PaginationFilter.defaultPagination());
    }

    @RequestMapping(value = "/rest/transaction/list", method = RequestMethod.POST)
    public List<TransactionDto> transaction(@RequestBody ReportFilters reportFilters) {
        return transactionService.find(DateFilter.generateDateFilter(reportFilters), reportFilters.getPaginationFilter());
    }

    @RequestMapping(value = "/rest/transaction/count", method = RequestMethod.GET)
    public long countTransaction() {
        return transactionService.countTransactions(DateFilter.currentCashPeriod());
    }

    @RequestMapping(value = "/rest/transaction/count", method = RequestMethod.POST)
    public long countTransaction(@RequestBody ReportFilters reportFilters) {
        return transactionService.countTransactions(DateFilter.generateDateFilter(reportFilters));
    }

    @RequestMapping(value = "/rest/transaction/add", method = RequestMethod.POST)
    public ResponseEntity<String> addTransaction(@RequestBody @Valid TransactionDto transaction, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>("Not correct sum", HttpStatus.BAD_REQUEST);
        transactionService.createTransactionTo(transaction.getSum());
        return new ResponseEntity<>("created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/transaction/complete/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> completedTransaction(@PathVariable("id") long id) {
        transactionService.completeTransaction(id);
        return new ResponseEntity<>("completed", HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/transaction/sumTransaction", method = RequestMethod.GET)
    public BigDecimal currentBalance() {
        return transactionService.sumTransaction(DateFilter.currentCashPeriod());
    }

    @RequestMapping(value = "/rest/transaction/sumTransaction", method = RequestMethod.POST)
    public BigDecimal currentBalance(@RequestBody ReportFilters reportFilters) {
        return transactionService.sumTransaction(DateFilter.generateDateFilter(reportFilters));
    }
}
