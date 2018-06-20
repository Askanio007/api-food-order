package dao;

import entity.Transaction;
import enums.TransactionStatus;
import org.springframework.stereotype.Repository;
import utils.DateFilter;
import utils.PaginationFilter;

import java.math.BigDecimal;
import java.util.List;

@Repository("transactionDao")
public class TransactionDAOImpl extends GenericDAOImpl<Transaction> implements TransactionDao {

    @Override
    public List<Transaction> listLastTransaction() {
        return createQuery("from Transaction order by createDate desc")
                .setMaxResults(10)
                .list();
    }

    @Override
    public List<Transaction> listTransaction(DateFilter filter, PaginationFilter paginationFilter) {
        return createQuery("from Transaction where createDate between :from and :to order by createDate desc")
                .setParameter("from", filter.getFrom())
                .setParameter("to", filter.getTo())
                .setFirstResult(paginationFilter.getOffset())
                .setMaxResults(paginationFilter.getLimit())
                .list();
    }

    @Override
    public long countTransaction(DateFilter filter) {
        Object count = createQuery("select count(*) from Transaction where createDate between :from and :to")
                .setParameter("from", filter.getFrom())
                .setParameter("to", filter.getTo())
                .getSingleResult();
        return count != null ? (long)count : 0;
    }

    @Override
    public BigDecimal sumTransaction(DateFilter filter, TransactionStatus status) {
        Object ob =  createQuery("select sum(t.transactionAmount) from Transaction as t join t.status as status where t.createDate between :from and :to and status.status = :status")
                .setParameter("from", filter.getFrom())
                .setParameter("to", filter.getTo())
                .setParameter("status",status)
                .uniqueResult();
        return getNumber(ob);
    }
}
