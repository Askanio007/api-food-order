package controller.v2;

import dto.FoodDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.FoodService;
import service.MenuService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/food")
@Api(value = "/api/v2/food", description = "Операции с едой")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @ApiOperation(value = "Блюда по заданному типу")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/byType", method = RequestMethod.POST, produces={"application/json; charset=UTF-8"})
    public ResponseEntity<ResponseServer> foodByTypes(@RequestBody String type) {
        return ResponseServer.OK(true, foodService.findAvailable(type));
    }

    @ApiOperation(value = "Блюда из заказа")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/byOrder/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> foodsByOrder(@PathVariable("id") long id) {
        return ResponseServer.OK(true, foodService.findById(id));
    }

    @ApiOperation(value = "Существование кода блюда")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/productCodeExist", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> foodExist(@RequestBody String code) {
        return ResponseServer.OK(true, foodService.isProductCodeExist(code));
    }

    @ApiOperation(value = "Добавление")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> addFood(@RequestBody FoodDto food) {
        foodService.add(food);
        return ResponseServer.OK(true, "Food was added");
    }

    @ApiOperation(value = "Список")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> foodList() {
        return ResponseServer.OK(true, foodService.findAllActive());
    }

    @ApiOperation(value = "Удаление")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> foodDeactivate(@PathVariable("id") long id) {
        if (foodService.canEditFood(id)) {
            foodService.deactivateFood(id);
            return ResponseServer.OK(true, "Food was deleted!");
        }
        return ResponseServer.OK(false, "", "Food in menu. Delete impossible");
    }

    @ApiOperation(value = "Список блюда по типу OTHER")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/other", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> listOtherFood() {
        return ResponseServer.OK(true, foodService.findOther());
    }

    @ApiOperation(value = "Редактирование")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> editFood(@PathVariable("id") long id) {
        if (foodService.canEditFood(id))
            return ResponseServer.OK(true, foodService.findDto(id));
        return ResponseServer.OK(false, "", "edit was failed");
    }

    @ApiOperation(value = "Цена блюда")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/price/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> foodPrice(@PathVariable("id") long id) {
        return ResponseServer.OK(true, foodService.priceFood(id));
    }

    @ApiOperation(value = "Сохранение изменений")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/edit/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseServer> editFood(@Valid @RequestBody FoodDto food, BindingResult result) {
        if(result.hasErrors())
            return ResponseServer.OK(false, "Food not edited!", result.getFieldErrors());
        foodService.editFood(food);
        return ResponseServer.OK(true, "Food was edited!");
    }

}
