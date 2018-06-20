package service;

import dao.ProviderOrdersDao;
import dto.ProviderOrderDto;
import entity.ProviderOrders;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProviderOrdersService {

    private static final Logger log = Logger.getLogger(ProviderOrdersService.class);

    @Autowired
    @Qualifier("providerOrders")
    private ProviderOrdersDao providerOrdersDao;

    @Transactional
    public void save(ProviderOrders po) {
        providerOrdersDao.save(po);
    }

    @Transactional
    public ProviderOrders getToday() {
        return providerOrdersDao.getToday();
    }

    @Transactional
    public boolean todayOrderIsSend() {
        return providerOrdersDao.todayOrderIsSend();
    }

    @Transactional
    public ProviderOrderDto getIdProviderOrder() {
        return ProviderOrderDto.convertToDto(getToday());
    }
}
