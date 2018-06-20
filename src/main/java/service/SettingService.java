package service;

import dao.SettingDao;
import dto.SettingDto;
import entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SettingService {

    @Autowired
    @Qualifier("settingDao")
    private SettingDao settingDao;

    private final String TIME_ATTEMPT = "timeAttempt";
    private final String CITY = "city";
    private final String HOUSE = "house";
    private final String STREET = "street";
    private final String FLOOR = "floor";
    private final String FLAT = "flat";
    private final String PHONE = "phone";
    private final String API_URL = "api";

    @Transactional
    protected Setting find(String name) {
        return settingDao.findByName(name);
    }

    @Transactional
    public List<SettingDto> find() {
        return SettingDto.convertToDto(settingDao.find());
    }

    @Transactional
    public SettingDto findByName(String name) {
        SettingDto settingDto = null;
        Setting setting = find(name);
        if (setting != null)
            settingDto = SettingDto.convertToDto(setting);
        return settingDto;
    }

    @Transactional
    public void editSettings(List<SettingDto> settings) {
        settings.forEach(setting -> {
            Setting set = find(setting.getName());
            set.setValue(setting.getValue());
            settingDao.update(set);
        });
    }

    private String findValue(String name) {
        SettingDto settingDto = findByName(name);
        if (settingDto != null)
            return settingDto.getValue();
        return "";
    }

    @Transactional
    public String getCountAttemptSending() {
        return findValue(TIME_ATTEMPT);
    }

    @Transactional
    public String getCity() {
        return findValue(CITY);
    }

    @Transactional
    public String getApiUrl() {
        return findValue(API_URL);
    }

    @Transactional
    public String getFloor() {
        return findValue(FLOOR);
    }

    @Transactional
    public String getStreet() {
        return findValue(STREET);
    }

    @Transactional
    public String getFlat() {
        return findValue(FLAT);
    }

    @Transactional
    public String getPhone() {
        return findValue(PHONE);
    }

    @Transactional
    public String getHouse() {
        return findValue(HOUSE);
    }
}
