package controller.v2;

import enums.StatusOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.MenuService;
import service.OrderService;
import service.ProviderOrdersService;
import service.UserService;

@RestController
@RequestMapping("/api/v2/manager")
@Api(value = "/api/v2/manager", description = "Операции распорядителя")
public class ManagerControllerV2 {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProviderOrdersService providerOrdersService;


    @ApiOperation(value = "Хватает суммы на счету для оплаты текущего заказа")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/haveEnoughMoney", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> haveEnoughMoney() {
        return ResponseServer.OK(true, orderService.haveEnoughMoney());
    }

    @ApiOperation(value = "Доставлен сегодняшний заказ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/isAccept", method = RequestMethod.GET)
    public boolean todayOrdersActive() {
        return orderService.todayOrdersAccept();
    }

    @ApiOperation(value = "Принять сегодняшний заказ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/accept", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> acceptOrder() {
        //if (!orderService.haveEnoughMoney())
        //    return new ResponseEntity<>("not enough money", HttpStatus.OK);
        orderService.acceptingTodayOrder();
        return ResponseServer.OK(true, "order was accepted");
    }

    @ApiOperation(value = "Текщий баланс распорядителя")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> currentBalanceManager() {
        return ResponseServer.OK(true, userService.currentBalance());
    }

    @ApiOperation(value = "Сегодняшний заказ отправлен провайдеру")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/today/isSending", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> todayIsSending() {
        return ResponseServer.OK(true, providerOrdersService.todayOrderIsSend());
    }

    @ApiOperation(value = "Повторно отправить заказ провайдеру")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/send", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> sendOrders() {
        menuService.repeatSendOrders();
        return ResponseServer.OK(true, "Task was created");
    }

    @ApiOperation(value = "ID заказа на стороне провайдера")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/responseProvider", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> responseProvider() {
        return ResponseServer.OK(true, providerOrdersService.getIdProviderOrder());
    }

    @ApiOperation(value = "Текущий статус заказа")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/order/status", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public ResponseEntity<ResponseServer> orderStatus() {
        return ResponseServer.OK(true, StatusOrder.getType(menuService.statusOrder()));
    }

    @ApiOperation(value = "[Утилитное] Добавляет всем пользователям запись в таблицу об автозаказе")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/autoOrder/add", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> autoOrder() {
        userService.addAutoOrder();
        return ResponseServer.OK(true, "Add was success");
    }

}
