package controller.v2;

import dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;
import utils.EncryptingString;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2")
@Api(value = "/api/v2", description = "Логин/регистрация")
public class AuthController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Производит логин, возвращает пользователя")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> login(@RequestBody UserDto userDto) throws Exception {
        String login = userDto.getLogin();
        String password = userDto.getPassword();
        if (login == null) {
            return ResponseServer.OK(false, "Not allowed", "Login or password is empty");
        }
        UserDto u = userService.find(login);
        if (u.getLogin() != null && password != null) {
            if (!u.isEnable())
                return ResponseServer.OK(false, "Not allowed", "User is not active");
            if (EncryptingString.getEncoder().matches(password, u.getPassword())) {
                return ResponseServer.authorized(userService.find(login));
            }
        }
        return ResponseServer.OK(false, "Not allowed", "Incorrect password or login");
    }

    @ApiOperation(value = "Регистрирует пользователя")
    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces={"application/json; charset=UTF-8"})
    public ResponseEntity<ResponseServer> registration(@RequestBody @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors())
            return ResponseServer.OK(false, "Registration error", "Incorrect password or login");
        if (userService.isLoginExist(userDto.getLogin()))
            return ResponseServer.OK(false, "Registration error", "Login already exist");
        if (!userDto.getLogin().matches("[A-Za-z0-9]{1,}"))
            return ResponseServer.OK(false, "Registration error", "Incorrect login");
        userService.createUser(userDto);
        return ResponseServer.OK(true, "Request for registration was sent. Please contact the administrator", userService.find(userDto.getLogin()));
    }

}
