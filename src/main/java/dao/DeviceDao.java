package dao;

import entity.Device;

import java.util.List;

public interface DeviceDao extends GenericDao<Device>  {

    List<Device> findCustomerDevice();

    List<Device> findCookDevice();

    List<Device>  findAdminDevice();

    Device find(String deviceId, String login);

    boolean isSubscribe(String login);

}
