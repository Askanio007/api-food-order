package controller.v2;

import dto.MenuDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.MenuService;
import service.OrderService;

import java.util.Date;

@RestController
@RequestMapping("/api/v2/menu")
@Api(value = "/api/v2/menu", description = "Операции с меню")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "Сегодняшнее меню")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public ResponseEntity<ResponseServer> menu() {
        if (menuService.todayMenuExist()) {
            return ResponseServer.OK(true, menuService.getTodayMenu());
        }
        return ResponseServer.OK(true, null);
    }

    @ApiOperation(value = "Добавить")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> addMenu(@RequestBody MenuDto menuDto) {
        Date date = new Date();
        if (menuDto.getDateAcceptOrderD().getTime() < date.getTime())
            return ResponseServer.OK(false, "Date must be greater than current");
        if (menuService.todayMenuExist())
            return ResponseServer.OK(false, "Menu already exist");
        menuService.addMenu(menuDto);
        return ResponseServer.OK(true, "Menu was added!");
    }

    @ApiOperation(value = "[Утилитное] Удаление сегодняшнего меню и заказов")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> deleteTodayMenu() {
        menuService.deleteMenu();
        orderService.deleteTodayOrders();
        return ResponseServer.OK(true, "Success");
    }
}
