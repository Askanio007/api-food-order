package controller;

import config.answerServer.AnswerServer;
import config.answerServer.ErrorAnswer;
import config.answerServer.SuccessAnswer;
import dto.FoodTypeDto;
import dto.SettingDto;
import dto.UserDto;
import enums.RoleType;
import models.RoleUser;
import models.filters.UserListFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.FoodTypeService;
import service.SettingService;
import service.UserService;
import utils.PaginationFilter;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodTypeService foodTypeService;

    @RequestMapping(method = RequestMethod.GET, value = "/rest/settings")
    public List<SettingDto> listSettings() {
        return settingService.find();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rest/settings")
    public ResponseEntity<AnswerServer> editSettings(@RequestBody List<SettingDto> settings) {
        settingService.editSettings(settings);
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "edit was successful"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rest/user/count")
    public long countUsers() {
        return userService.countUser();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rest/user/count")
    public long countUsers(@RequestBody UserListFilters userListFilters) {
        return userService.countUser(userListFilters.getLogin(), userListFilters.getRole());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rest/users")
    public List<UserDto> listUsers(@RequestBody UserListFilters userListFilters) {
        return userService.findAll(userListFilters.getPaginationFilter(), userListFilters.getRole(), userListFilters.getLogin());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rest/users")
    public List<UserDto> listUsers() {
        return userService.findAll(PaginationFilter.defaultPagination());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rest/user/roles")
    public List<RoleUser> listRoles() {
        return RoleType.getListRoles();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rest/user/add")
    public ResponseEntity<AnswerServer> addUser(@RequestBody @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new ErrorAnswer(HttpStatus.BAD_REQUEST, "add was failed", result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }
        if (userService.isLoginExist(userDto.getLogin()))
            return new ResponseEntity<>(new ErrorAnswer(HttpStatus.BAD_REQUEST, "user already exist",result.getAllErrors()), HttpStatus.BAD_REQUEST);
        userService.createUser(userDto);
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "add was successful"), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/types", method = RequestMethod.GET)
    public List<FoodTypeDto> getTypes() {
        return foodTypeService.getAllTypes();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rest/blockedUser")
    public ResponseEntity<AnswerServer> blockedUser(@RequestBody long id) {
        userService.blockedUser(id);
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "user was blocked"), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/type/add", method = RequestMethod.POST)
    public ResponseEntity<AnswerServer> addTypes(@RequestBody @Valid FoodTypeDto foodTypeDto, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>(new ErrorAnswer(HttpStatus.BAD_REQUEST, "type is empty", result.getAllErrors()), HttpStatus.BAD_REQUEST);
        if(foodTypeService.typeExist(foodTypeDto.getType()))
            return new ResponseEntity<>(new ErrorAnswer(HttpStatus.BAD_REQUEST, "type already exist", result.getAllErrors()), HttpStatus.BAD_REQUEST);
        foodTypeService.save(foodTypeDto);
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "type was added"), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rest/activatedUser")
    public ResponseEntity<AnswerServer> activatedUser(@RequestBody long id) {
        userService.activatedUser(id);
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "user was activated"), HttpStatus.OK);
    }


}
