package dao;

import entity.ProviderOrders;

public interface ProviderOrdersDao extends GenericDao<ProviderOrders> {

    ProviderOrders getToday();

    boolean todayOrderIsSend();
}
