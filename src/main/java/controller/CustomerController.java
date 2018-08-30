package controller;

import config.answerServer.AnswerServer;
import config.answerServer.ErrorAnswer;
import config.answerServer.SuccessAnswer;
import converter.MoneyToString;
import dto.AutoOrderDto;
import dto.FoodDto;
import dto.OrderDto;
import dto.UserDto;
import enums.StatusOrder;
import models.filters.ReportFilters;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scheduler.SendOrderInFoodProvider;
import service.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static controller.UserController.getCurrentUserName;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private static final Logger log = Logger.getLogger(CustomerController.class);

    private static final BigDecimal MONEY_LIMIT_OF_MONTH = new BigDecimal(6000.00);

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private MessageService messageService;


    @RequestMapping(value = "/rest/customer/order", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public OrderDto order() {
        return orderService.findToday(getCurrentUserName());
    }

    @RequestMapping(value = "/rest/customer/sumOrder", method = RequestMethod.GET)
    public String sumOrder() {
        return MoneyToString.convert(orderService.sumTodayOrder(getCurrentUserName()));
    }

    @RequestMapping(value = "/rest/customer/limit", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public String monthLimit() {
        return MONEY_LIMIT_OF_MONTH.toString();
    }

    @RequestMapping(value = "/rest/customer/balance", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public String userBalance() {
        UserDto user = userService.find(getCurrentUserName());
        return user.getBalance().toString();
    }

    @RequestMapping(value = "/rest/customer/getFoodsByType", method = RequestMethod.POST, produces={"application/json; charset=UTF-8"})
    public List<FoodDto> foodByTypes(@RequestBody String type) {
        return foodService.findAvailable(type);
    }

    @RequestMapping(value = "/rest/customer/createOrder", method = RequestMethod.POST)
    public ResponseEntity<String> addOrder(@RequestBody List<FoodDto> foods)  {
        if (menuService.statusOrder() == StatusOrder.WAITING_DELIVERY) {
            log.error("menu was accepted. Create order is forbidden. USER: " + getCurrentUserName());
            return new ResponseEntity<>("menu was accepted. Create order is forbidden", HttpStatus.OK);
        }
        if (orderService.findToday(getCurrentUserName()) != null) {
            log.error("menu was accepted. Create order is forbidden. USER: " + getCurrentUserName());
            return new ResponseEntity<>("order already exist", HttpStatus.OK);
        }
        UserDto user = userService.find(getCurrentUserName());
        if (orderService.calculatePriceOrder(foods).compareTo(user.getBalance()) > 0) {
            log.error("not enough money. USER: " + getCurrentUserName());
            return new ResponseEntity<>("not enough money", HttpStatus.OK);
        }
        orderService.save(foods, getCurrentUserName());
        log.info("order was created. USER: " + getCurrentUserName());
        return new ResponseEntity<>("order was created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/customer/edit", method = RequestMethod.POST) //[{"id":"13","type":"Другое","price":"350.00"},{"id":"12","type":"Другое","price":"228.00"}]
    public ResponseEntity<AnswerServer> editOrder(@RequestBody List<FoodDto> foods) {
        if (menuService.statusOrder() == StatusOrder.WAITING_DELIVERY)
            return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "menu was accepted. Edit order is forbidden"), HttpStatus.OK);
        UserDto user = userService.find(getCurrentUserName());
        if (orderService.calculatePriceOrder(foods).compareTo(user.getBalance()) > 0)
            return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "not enough money"), HttpStatus.OK);
        orderService.update(foods, getCurrentUserName());
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.CREATED, "order was edited"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/customer/autoOrder", method = RequestMethod.POST)
    public ResponseEntity<AnswerServer> addAutoOrder(@RequestBody AutoOrderDto autoOrderDto)  {
        if (autoOrderDto.getFoods() == null && autoOrderDto.getType() == null)
            return new ResponseEntity<>(new ErrorAnswer(HttpStatus.OK, "error valid autoorder", "error"), HttpStatus.OK);
        userService.saveAutoOrder(autoOrderDto, getCurrentUserName());
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.CREATED, "Auto-order was created"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/customer/autoOrder", method = RequestMethod.GET)
    public AutoOrderDto getAutoOrder()  {
        return userService.getAutoOrder(getCurrentUserName());
    }

    @RequestMapping(value = "/rest/customer/autoOrder/disable", method = RequestMethod.GET)
    public ResponseEntity<AnswerServer> disableAutoOrder()  {
        userService.disableAutoOrder(getCurrentUserName());
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "Auto-order was disable"), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/customer/hasAutoOrder", method = RequestMethod.GET)
    public boolean hasAutoOrder()  {
        return userService.hasAutoOrder(getCurrentUserName());
    }

    @RequestMapping(value = "/rest/customer/saveDeviceId", method = RequestMethod.POST)
    public ResponseEntity<AnswerServer> saveDevice(@RequestBody String deviceId)  {
        userService.saveDevice(deviceId, getCurrentUserName());
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "Device was added"), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/customer/deleteDeviceId", method = RequestMethod.POST)
    public ResponseEntity<AnswerServer> deleteDevice(@RequestBody String deviceId)  {
        userService.deleteDevice(deviceId, getCurrentUserName());
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "Device was deleted"), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/customer/lastPeriodBalance", method = RequestMethod.GET)
    public BigDecimal lastPeriodBalance() {
        return orderService.lastPeriodBalance(getCurrentUserName());
    }

    @RequestMapping(value = "/rest/customer/order/delete", method = RequestMethod.GET)
    public ResponseEntity<AnswerServer> deleteOrder() {
        if (menuService.statusOrder() != StatusOrder.WAITING_DELIVERY) {
            orderService.deleteOrder(getCurrentUserName());
            return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "Order was delete success"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "Order not delete"), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/customer/count", method = RequestMethod.GET)
    public long countCustomer() {
        return userService.countCustomer();
    }

    @RequestMapping(value = "/rest/customer/count", method = RequestMethod.POST)
    public long countCustomer(@RequestBody ReportFilters reportFilters) {
        return userService.countCustomer(reportFilters);
    }

}