package dao;

import entity.ProviderOrders;
import enums.RequestState;
import org.springframework.stereotype.Repository;
import static utils.DateBuilder.*;

@Repository("providerOrders")
public class ProviderOrdersDAOImpl extends GenericDAOImpl<ProviderOrders> implements ProviderOrdersDao {

    @Override
    public ProviderOrders getToday() {
        Object ob = createQuery("from ProviderOrders where dateSend between :from and :to")
                .setParameter("from", startDay(today()))
                .setParameter("to", endDay(today()))
                .uniqueResult();
        return ob != null ? (ProviderOrders) ob : new ProviderOrders();
    }

    @Override
    public boolean todayOrderIsSend() {
        Object ob = createQuery("from ProviderOrders where requestState = :state and dateSend between :from and :to")
                .setParameter("from", startDay(today()))
                .setParameter("to", endDay(today()))
                .setParameter("state", RequestState.FAILED)
                .uniqueResult();
        if (ob == null)
            return true;
        ProviderOrders po = (ProviderOrders) ob;
        return !(po.getResponse() == null || po.getIdOrder() == null || po.getCodeOrder() == null || "".equals(po.getCodeOrder()) || "".equals(po.getIdOrder()));
    }
}
