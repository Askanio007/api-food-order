package service;

import dao.DeviceDao;
import entity.Device;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DeviceService {

    @Autowired
    @Qualifier("deviceDao")
    private DeviceDao deviceDao;

    @Transactional
    public void save(Device device) {
        deviceDao.save(device);
    }

    @Transactional
    public List<Device> allDevices() {
        return deviceDao.find();
    }
}
