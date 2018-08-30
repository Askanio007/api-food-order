package dao;

import entity.Device;
import enums.RoleType;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import java.util.List;

@Repository("deviceDao")
public class DeviceDAOImpl extends GenericDAOImpl<Device> implements DeviceDao {

    @Override
    public List<Device> findCustomerDevice() {
        return listDevices(RoleType.ROLE_USER.getId());
    }

    @Override
    public List<Device> findCookDevice() {
        return listDevices(RoleType.ROLE_COOK.getId());
    }

    @Override
    public List<Device> findAdminDevice() {
        return listDevices(RoleType.ROLE_ADMIN.getId());
    }

    private List<Device> listDevices(long roleUserId) {
        return createQuery("select d " +
                "from Device as d " +
                "join d.user as u " +
                "join u.role as role " +
                "where role.id = :roleId").setParameter("roleId", roleUserId).list();
    }

    @Override
    public Device find(String deviceId, String login) {
        Object ob = createQuery("select d from Device as d join d.user as u where d.deviceId = :device and u.login = :login")
                .setParameter("device", deviceId)
                .setParameter("login", login)
                .uniqueResult();
        return ob != null ? (Device) ob : null;
    }

    @Override
    public boolean isSubscribe(String login) {
        List ob = createQuery("from Device as d join d.user as u where u.login = :login").setParameter("login", login).list();
        return !ob.isEmpty();
    }
}
