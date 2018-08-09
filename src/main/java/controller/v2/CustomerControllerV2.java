package controller.v2;

import dto.AutoOrderDto;
import dto.FoodDto;
import dto.UserDto;
import enums.StatusOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import models.filters.ReportFilters;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.MenuService;
import service.OrderService;
import service.UserService;

import java.util.List;

import static controller.v2.UserControllerV2.getCurrentUserLogin;

@RestController
@RequestMapping("/api/v2/customer")
@Api(value = "/api/v2/customer", description = "Операции едоков")
public class CustomerControllerV2 {

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Сумма текущего заказа")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/sum", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> sumOrder() {
        return ResponseServer.OK(true, orderService.sumTodayOrder(getCurrentUserLogin()));
    }

    @ApiOperation(value = "Общее количество едоков")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> countCustomer() {
        return ResponseServer.OK(true, userService.countCustomer());
    }

    @ApiOperation(value = "Количество едоков с учётом фильтров")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> countCustomer(@RequestBody ReportFilters reportFilters) {
        return ResponseServer.OK(true, userService.countCustomer(reportFilters));
    }

    @ApiOperation(value = "Сумма затрат за прошлый отчётный период")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/lastPeriodBalance", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> lastPeriodBalance() {
        return ResponseServer.OK(true, orderService.lastPeriodBalance(getCurrentUserLogin()));
    }

    @ApiOperation(value = "Добавление автозаказа")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/autoOrder", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> addAutoOrder(@RequestBody AutoOrderDto autoOrderDto)  {
        if (autoOrderDto.getFoods() == null && autoOrderDto.getType() == null)
            return ResponseServer.OK(false, "", "type or list food is null");
        userService.saveAutoOrder(autoOrderDto, getCurrentUserLogin());
        return ResponseServer.OK(true, "Auto-order was created");
    }

    @ApiOperation(value = "Информация об автозаказе")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/autoOrder", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> getAutoOrder()  {
        return ResponseServer.OK(true, userService.getAutoOrder(getCurrentUserLogin()));
    }

    @ApiOperation(value = "Отключение автозаказа")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/autoOrder/disable", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> disableAutoOrder()  {
        userService.disableAutoOrder(getCurrentUserLogin());
        return ResponseServer.OK(true, "Auto-order was disable");
    }

    @ApiOperation(value = "Существует ли автозаказ у пользователя")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/autoOrder/exist", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> hasAutoOrder()  {
        return ResponseServer.OK(true, userService.hasAutoOrder(getCurrentUserLogin()));
    }

    @ApiOperation(value = "Сегодняшний/текущий заказ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public ResponseEntity<ResponseServer> order() {
        return ResponseServer.OK(true, orderService.findToday(getCurrentUserLogin()));
    }

    @ApiOperation(value = "Создать заказ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> addOrder(@RequestBody List<FoodDto> foods)  {
        if (menuService.statusOrder() == StatusOrder.WAITING_DELIVERY)
            return ResponseServer.OK(false, "menu was accepted. Create order is forbidden");
        if (orderService.findToday(getCurrentUserLogin()) != null)
            return ResponseServer.OK(false, "order already exist");
        UserDto user = userService.find(getCurrentUserLogin());
        if (orderService.calculatePriceOrder(foods).compareTo(user.getBalance()) > 0)
            return ResponseServer.OK(false, "not enough money");
        orderService.save(foods, getCurrentUserLogin());
        return ResponseServer.OK(true, "order was created");
    }

    @ApiOperation(value = "Редактирование заказа")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/edit", method = RequestMethod.POST) //[{"id":"13","type":"Другое","price":"350.00"},{"id":"12","type":"Другое","price":"228.00"}]
    public ResponseEntity<ResponseServer> editOrder(@RequestBody List<FoodDto> foods) {
        if (menuService.statusOrder() == StatusOrder.WAITING_DELIVERY)
            return ResponseServer.OK(false, "menu was accepted. Edit order is forbidden");
        orderService.update(foods, getCurrentUserLogin());
        return ResponseServer.OK(true, "order was edited");
    }

    @ApiOperation(value = "Закрепление девайся пользователю для пуш уведомлений")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/notification/device/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> saveDevice(@RequestBody String deviceId)  {
        userService.saveDevice(deviceId, getCurrentUserLogin());
        return ResponseServer.OK(true, "Device was added");
    }

    @ApiOperation(value = "Удаление заказа")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/delete", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> deleteOrder() {
        if (menuService.statusOrder() != StatusOrder.WAITING_DELIVERY) {
            orderService.deleteOrder(getCurrentUserLogin());
            return ResponseServer.OK(true,  "Order was delete success");
        }
        return ResponseServer.OK(false, "Order already sent to food-provider");
    }

    @ApiOperation(value = "Сохранение имени")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/name/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> saveName(@RequestBody String name) {
        userService.saveName(getCurrentUserLogin(), name);
        return ResponseServer.OK(true, "Name saved success");
    }

    @ApiOperation(value = "Текущий баланс")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> currentBalance() {
        UserDto user = userService.find(getCurrentUserLogin());
        return ResponseServer.OK(true, user.getBalance());
    }
}
