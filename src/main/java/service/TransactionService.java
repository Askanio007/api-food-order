package service;

import dao.TransactionDao;
import dao.TransactionStatusDao;
import dto.TransactionDto;
import entity.Transaction;
import enums.TransactionStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import utils.DateFilter;
import utils.PaginationFilter;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger log = Logger.getLogger(TransactionService.class);

    @Autowired
    @Qualifier("transactionDao")
    private TransactionDao transactionDao;

    @Autowired
    @Qualifier("transactionStatusDao")
    private TransactionStatusDao transactionStatusDao;

    @Autowired
    private UserService userService;

    @Transactional
    public void createTransactionTo(BigDecimal sum, String comment) {
        Transaction tr = Transaction.createTransaction(sum, transactionStatusDao.find(TransactionStatus.SEND_TO_MM.getId()), comment);
        transactionDao.save(tr);
    }

    @Transactional
    public void completeTransaction(long id) {
        try {
            Transaction tr = transactionDao.find(id);
            tr.setStatus(transactionStatusDao.find(TransactionStatus.COMPLETE.getId()));
            userService.depositMoney(tr.getTransactionAmount());
            tr.setUpdateDate(new Date());
            transactionDao.update(tr);
        } catch (Exception e) {
            log.error("change status or update balance is failed!", e);
        }
    }

    @Transactional
    public void paymentTransaction(BigDecimal sum) {
        try {
            userService.withdrawMoney(sum);
            Transaction tr = Transaction.createTransaction(sum, transactionStatusDao.find(TransactionStatus.PAYMENT_ORDER.getId()), "Оплата заказа");
            transactionDao.save(tr);
        } catch (Exception e) {
            log.error("create transaction or update balance is failed!", e);
        }
    }

    @Transactional
    public List<TransactionDto> findLast() {
        return TransactionDto.convertToDto(transactionDao.listLastTransaction());
    }

    @Transactional
    public List<TransactionDto> find(DateFilter filter, PaginationFilter paginationFilter) {
        return TransactionDto.convertToDto(transactionDao.listTransaction(filter, paginationFilter));
    }

    @Transactional
    public long countTransactions(DateFilter dateFilter) {
        return transactionDao.countTransaction(dateFilter);
    }

    @Transactional
    public BigDecimal sumTransaction(DateFilter filter) {
        BigDecimal complete = transactionDao.sumTransaction(filter, TransactionStatus.COMPLETE);
        BigDecimal payment = transactionDao.sumTransaction(filter, TransactionStatus.PAYMENT_ORDER);
        return complete.subtract(payment);
    }

}
