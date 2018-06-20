package controller;

import dto.ProviderOrderDto;
import models.xmlOrder.XMLOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import service.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class ManagerController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProviderOrdersService providerOrdersService;


    @RequestMapping(value = "/rest/haveEnoughMoney", method = RequestMethod.GET)
    public boolean haveEnoughMoney() {
        return orderService.haveEnoughMoney();
    }

    @RequestMapping(value = "/rest/acceptOrders", method = RequestMethod.GET)
    public ResponseEntity<String> acceptOrder() {
        //if (!orderService.haveEnoughMoney())
        //    return new ResponseEntity<>("not enough money", HttpStatus.OK);
        orderService.acceptingTodayOrder();
        return new ResponseEntity<>("order was accepted", HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/user/currentBalance", method = RequestMethod.GET)
    public BigDecimal currentBalanceManager() {
        return userService.currentBalance();
    }

    @RequestMapping(value = "/rest/deleteTodayMenu", method = RequestMethod.GET)
    public String deleteTodayMenu() {
        menuService.deleteMenu();
        orderService.deleteTodayOrders();
        return "success";
    }

    @RequestMapping(value = "/rest/order/todayIsSending", method = RequestMethod.GET)
    public boolean todayIsSending() {
        return providerOrdersService.todayOrderIsSend();
    }

    @RequestMapping(value = "/rest/sendOrders", method = RequestMethod.GET)
    public ResponseEntity<String> sendOrders() {
        menuService.repeatSendOrders();
        return new ResponseEntity<>("Task was created", HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/responseProvider", method = RequestMethod.GET)
    public ProviderOrderDto responseProvider() {
        return providerOrdersService.getIdProviderOrder();
    }
}