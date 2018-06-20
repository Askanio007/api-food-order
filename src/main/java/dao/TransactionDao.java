package dao;

import entity.Transaction;
import enums.TransactionStatus;
import utils.DateFilter;
import utils.PaginationFilter;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionDao extends GenericDao<Transaction> {

    List<Transaction> listLastTransaction();

    List<Transaction> listTransaction(DateFilter filter, PaginationFilter paginationFilter);

    long countTransaction(DateFilter filter);

    BigDecimal sumTransaction(DateFilter filter, TransactionStatus status);

}
