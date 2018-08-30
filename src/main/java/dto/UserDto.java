package dto;

import converter.MoneyToString;
import entity.User;
import enums.RoleType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter@Getter
public class UserDto {

    private long id;

    @Size(min = 3, max = 15)
    private String login;

    private String balanceView;

    private boolean enable;

    private BigDecimal balance;

    @Size(min = 6, max = 15, message = "Пароль может содержать от 6 до 15 символов")
    private String password;

    private String name;

    private RoleType role;

    public UserDto() {}

    private UserDto(User user) {
        if (user != null) {
            this.id = user.getId();
            this.enable = user.isEnabled();
            this.name = user.getName();
            this.login = user.getLogin();
            this.balance = user.getBalance();
            this.balanceView = MoneyToString.convert(user.getBalance());
            this.role = RoleType.getRoleType(user.getRole().getRole());
        }
    }

    public static UserDto convertToDto(User user) {
        return new UserDto(user);
    }

    public static List<UserDto> convertToDto(List<User> users) {
        List<UserDto> userDto = new ArrayList<>();
        users.forEach(user -> userDto.add(convertToDto(user)));
        return userDto;
    }

    public String getRoleView() {
        return RoleType.roleAsString(this.role);
    }


}
