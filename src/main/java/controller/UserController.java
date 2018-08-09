package controller;

import config.securityConfig.TokenUtil;
import config.answerServer.AnswerServer;
import config.answerServer.ErrorAnswer;
import config.answerServer.SuccessAnswer;
import dto.UserDto;
import enums.StatusOrder;
import models.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.MenuService;
import service.UserService;
import utils.EncryptingString;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    protected static String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "/rest/user", method = RequestMethod.GET)
    public UserDto profile() {
        return userService.find(getCurrentUserName());
    }

    @RequestMapping(value = "/rest/user/saveName", method = RequestMethod.POST)
    public ResponseEntity<String> saveName(@RequestBody String name) {
        userService.saveName(getCurrentUserName(), name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/login", method = RequestMethod.POST)
    public ResponseEntity<AnswerServer> login(@RequestBody UserDto userDto) throws Exception {
        String login = userDto.getLogin();
        String password = userDto.getPassword();
        if (login == null) {
            return new ResponseEntity<>(new ErrorAnswer(HttpStatus.UNAUTHORIZED, "Not allowed", "login is empty"), HttpStatus.UNAUTHORIZED);
        }
        UserDto u = userService.find(login);
        if (u.getLogin() != null && password != null) {
            if (!u.isEnable())
                return new ResponseEntity<>(new ErrorAnswer(HttpStatus.UNAUTHORIZED, "Not allowed", "user is not active"), HttpStatus.UNAUTHORIZED);
            if (EncryptingString.getEncoder().matches(password, u.getPassword())) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", TokenUtil.generateToken(u));
                return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "success", userService.find(login)), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorAnswer(HttpStatus.UNAUTHORIZED, "Not allowed", "incorrect password or login"), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(new ErrorAnswer(HttpStatus.UNAUTHORIZED, "Not allowed", "incorrect password or login"), HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/rest/registration", method = RequestMethod.POST, produces={"application/json; charset=UTF-8"})
    public ResponseEntity<String> registration(@RequestBody @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>("Некорректный логин или пароль", HttpStatus.BAD_REQUEST);
        if (userService.isLoginExist(userDto.getLogin()))
            return new ResponseEntity<>("Имя пользователя уже существует", HttpStatus.BAD_REQUEST);
        if (!userDto.getLogin().matches("[A-Za-z0-9]{1,}"))
            return new ResponseEntity<>("Имя пользователя может состоять из латинских букв и/или цифр", HttpStatus.BAD_REQUEST);
        userService.createUser(userDto);
        return new ResponseEntity<>("Запрос на регистрацию отправлен. Для входа обратитесь к администратору.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/orderStatus", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public ResponseEntity orderStatus() {
        return new ResponseEntity<>(StatusOrder.getType(menuService.statusOrder()), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/addAutoOrder", method = RequestMethod.GET)
    public String autoOrder() {
        userService.addAutoOrder();
        return "ok";
    }

    @RequestMapping(value = "/rest/password/change", method = RequestMethod.POST, produces={"application/json; charset=UTF-8"})
    public ResponseEntity<String> changePassword(@RequestBody @Valid Password password, BindingResult result) {
        if(result.hasErrors()) {
            return new ResponseEntity<>("Некорректный новый пароль", HttpStatus.BAD_REQUEST);
        }
        if (!password.getNewPassword().equals(password.getConfirmNewPassword())) {
            return new ResponseEntity<>("Пароли не совпадают", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password.getUserId(), password.getNewPassword());
        return new ResponseEntity<>("Пароль успешно изменён", HttpStatus.OK);
    }


}

