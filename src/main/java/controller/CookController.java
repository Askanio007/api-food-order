package controller;

import config.answerServer.AnswerServer;
import config.answerServer.ErrorAnswer;
import config.answerServer.SuccessAnswer;
import dto.FoodDto;
import dto.MenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CookController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodTypeService foodTypeService;

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/rest/menu", method = RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public MenuDto menu() {
        if (menuService.todayMenuExist()) {
            return menuService.getTodayMenu();
        }
        return null;
    }

    @RequestMapping(value = "/rest/menu", method = RequestMethod.POST)
    public ResponseEntity<AnswerServer> addMenu(@RequestBody MenuDto menuDto) {
        Date date = new Date();
        if (menuDto.getDateAcceptOrderD().getTime() < date.getTime())
            return new ResponseEntity<>(new ErrorAnswer(HttpStatus.OK, "error", "Date must be greater than current"), HttpStatus.OK);
        if (menuService.todayMenuExist())
            return new ResponseEntity<>(new ErrorAnswer(HttpStatus.OK, "error", "Menu already exist"), HttpStatus.OK);
        menuService.addMenu(menuDto);
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "Menu was added!"),HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/cook/getFoodsByOrder/{id}", method = RequestMethod.GET)
    public List<FoodDto> foodsByOrder(@PathVariable("id") long id) {
        return foodService.findById(id);
    }

    @RequestMapping(value = "/rest/productCodeExist", method = RequestMethod.POST)
    public boolean foodExist(@RequestBody String code) {
        return foodService.isProductCodeExist(code);
    }

    @RequestMapping(value = "/rest/food/add", method = RequestMethod.POST)
    public ResponseEntity<String> addFood(@RequestBody FoodDto food) {
        foodService.add(food);
        return new ResponseEntity<>("Food was added", HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/food/list", method = RequestMethod.GET)
    public List<FoodDto> foodList() {
        return foodService.findAllActive();
    }

    @RequestMapping(value = "/rest/food/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> foodDeactivate(@PathVariable("id") long id) {
        if (foodService.canEditFood(id)) {
            foodService.deactivateFood(id);
            return new ResponseEntity<>("Food was deleted!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Food in menu. Delete impossible", HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/food/types", method = RequestMethod.GET)
    public List<String> getTypes() {
        return foodTypeService.getFoodTypes();
    }

    @RequestMapping(value = "/rest/food/allTypes", method = RequestMethod.GET)
    public List<String> allTypes() {
        return foodTypeService.getAllFoodTypes();
    }

    @RequestMapping(value = "/rest/food/other", method = RequestMethod.GET)
    public List<FoodDto> listOtherFood() {
        return foodService.findOther();
    }

    @RequestMapping(value = "/rest/food/edit/{id}", method = RequestMethod.GET)
    public FoodDto editFood(@PathVariable("id") long id) {
        if (foodService.canEditFood(id))
            return foodService.findDto(id);
        return null;
    }

    @RequestMapping(value = "/rest/food/price/{id}", method = RequestMethod.GET)
    public BigDecimal foodPrice(@PathVariable("id") long id) {
        return foodService.priceFood(id);
    }

    @RequestMapping(value = "/rest/food/saveEdit", method = RequestMethod.POST)
    public ResponseEntity<AnswerServer> editFood(@Valid @RequestBody FoodDto food, BindingResult result) {
        if(result.hasErrors())
            return new ResponseEntity<>(new ErrorAnswer(HttpStatus.OK, "Food not edited!", result.getFieldErrors()), HttpStatus.OK);
        foodService.editFood(food);
        return new ResponseEntity<>(new SuccessAnswer(HttpStatus.OK, "Food was edited!"), HttpStatus.OK);
    }

}
