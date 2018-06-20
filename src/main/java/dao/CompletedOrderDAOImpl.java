package dao;

import entity.CompletedOrder;
import org.springframework.stereotype.Repository;

@Repository("completedOrder")
public class CompletedOrderDAOImpl extends GenericDAOImpl<CompletedOrder> implements CompletedOrderDao {


}
