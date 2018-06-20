package dao;

import entity.Setting;
import org.springframework.stereotype.Repository;

@Repository("settingDao")
public class SettingDAOImpl extends GenericDAOImpl<Setting> implements SettingDao {

    @Override
    public Setting findByName(String name) {
        Object ob = createQuery("from Setting where name = :name").setParameter("name", name).uniqueResult();
        return ob != null ? (Setting) ob : null;
    }
}
