package dao;

import entity.TransactionStatus;
import org.springframework.stereotype.Repository;

@Repository("transactionStatusDao")
public class TransactionStatusDAOImpl extends GenericDAOImpl<TransactionStatus> implements TransactionStatusDao {
}
