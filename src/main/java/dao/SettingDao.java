package dao;

import entity.Setting;

public interface SettingDao extends GenericDao<Setting> {

    Setting findByName(String name);
}
