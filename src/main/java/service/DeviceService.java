package service;

import dao.DeviceDao;
import entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    @Transactional
    public List<Device> getCookDevices() {
        return deviceDao.findCookDevice();
    }

    @Transactional
    public List<Device> getCustomerDevices() {
        return deviceDao.findCustomerDevice();
    }

    public List<String> convertDevice(List<Device> devices) {
        List<String> listDevice = new ArrayList<>();
        devices.forEach(device -> listDevice.add(device.getDeviceId()));
        return listDevice;
    }

    @Transactional
    public Device find(String deviceId, String login) {
        return deviceDao.find(deviceId, login);
    }

    @Transactional
    public void delete(Device device) {
        deviceDao.delete(device.getId());
    }

    @Transactional
    public boolean isSubscribe(String login) {
        return deviceDao.isSubscribe(login);
    }
}
