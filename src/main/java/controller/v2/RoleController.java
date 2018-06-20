package controller.v2;

import enums.RoleType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import models.responseServer.ResponseServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/role")
@Api(value = "/api/v2/role", description = "Операции с ролями")
public class RoleController {

    @ApiOperation(value = "Список")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity<ResponseServer> listRoles() {
        return ResponseServer.OK(true, RoleType.getListRoles());
    }
}
