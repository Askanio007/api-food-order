package models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter

public class Password {

    @NotNull
    private long userId;

    @Size(min = 6, max = 15, message = "Пароль может содержать от 6 до 15 символов")
    private String newPassword;

    @Size(min = 6, max = 15, message = "Пароль может содержать от 6 до 15 символов")
    private String confirmNewPassword;

    public Password() {
    }
}
