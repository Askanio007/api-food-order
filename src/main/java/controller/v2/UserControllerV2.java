package controller.v2;

import dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import models.filters.UserListFilters;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;
import utils.PaginationFilter;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/user")
@Api(value = "/api/v2/user", description = "Операции с пользователями системы")
public class UserControllerV2 {

    @Autowired
    private UserService userService;

    protected static String getCurrentUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @ApiOperation(value = "Данные пользователя")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> profile() {
        return ResponseServer.OK(true, userService.find(getCurrentUserLogin()));
    }

    @ApiOperation(value = "Количество")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.GET, value = "/count")
    public ResponseEntity<ResponseServer> countUsers() {
        return ResponseServer.OK(true, userService.countUser());
    }

    @ApiOperation(value = "Количество с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/count")
    public ResponseEntity<ResponseServer> countUsers(@RequestBody UserListFilters userListFilters) {
        return ResponseServer.OK(true, userService.countUser(userListFilters.getLogin(), userListFilters.getRole()));
    }

    @ApiOperation(value = "Список с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public ResponseEntity<ResponseServer> listUsers(@RequestBody UserListFilters userListFilters) {
        return ResponseServer.OK(true, userService.findAll(userListFilters.getPaginationFilter(), userListFilters.getRole(), userListFilters.getLogin()));
    }

    @ApiOperation(value = "Список")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity<ResponseServer> listUsers() {
        return ResponseServer.OK(true, userService.findAll(PaginationFilter.defaultPagination()));
    }

    @ApiOperation(value = "Добавление")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public ResponseEntity<ResponseServer> addUser(@RequestBody @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseServer.OK(false, "add was failed", result.getAllErrors());
        }
        if (userService.isLoginExist(userDto.getLogin()))
            return ResponseServer.OK(false, "user already exist", result.getAllErrors());
        userService.createUser(userDto);
        return ResponseServer.OK(true, "add was successful");
    }

    @ApiOperation(value = "Заблокировать")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/block")
    public ResponseEntity<ResponseServer> blockedUser(@RequestBody long id) {
        userService.blockedUser(id);
        return ResponseServer.OK(true, "user was blocked");
    }

    @ApiOperation(value = "Активировать")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/activate")
    public ResponseEntity<ResponseServer> activatedUser(@RequestBody long id) {
        userService.activatedUser(id);
        return ResponseServer.OK(true, "user was activated");
    }
}
