package dao;

import entity.Device;
import org.springframework.stereotype.Repository;

@Repository("deviceDao")
public class DeviceDAOImpl extends GenericDAOImpl<Device> implements DeviceDao {
}
