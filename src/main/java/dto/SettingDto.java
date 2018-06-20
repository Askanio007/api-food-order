package dto;

import entity.Setting;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SettingDto {

    private String name;
    private String value;

    public SettingDto() {}

    public SettingDto(Setting setting) {
        this.name = setting.getName();
        this.value = setting.getValue();
    }

    public static SettingDto convertToDto(Setting setting) {
        return new SettingDto(setting);
    }

    public static List<SettingDto> convertToDto(List<Setting> settings) {
        List<SettingDto> list = new ArrayList<>();
        settings.forEach(setting -> list.add(convertToDto(setting)));
        return list;
    }
}
