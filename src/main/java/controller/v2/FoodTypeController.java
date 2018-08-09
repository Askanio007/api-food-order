package controller.v2;

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
import service.FoodTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/food/type")
@Api(value = "/api/v2/food/type", description = "Операции с типами еды")
public class FoodTypeController {

    @Autowired
    private FoodTypeService foodTypeService;

    @ApiOperation(value = "Все типы (строками)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> getAllTypes() {
        return ResponseServer.OK(true, foodTypeService.getAllTypes());
    }

    @ApiOperation(value = "Все типы (объектами)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/allDto", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> getAllTypesDto() {
        return ResponseServer.OK(true, foodTypeService.getAllFoodTypesDto());
    }

    @ApiOperation(value = "Доступные типы на этом этапе заказа")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public ResponseEntity<ResponseServer> getTypes() {
        return ResponseServer.OK(true, foodTypeService.getFoodTypes());
    }
}
