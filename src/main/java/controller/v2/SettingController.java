package controller.v2;

import dto.SettingDto;
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
import service.SettingService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/setting")
@Api(value = "/api/v2/setting", description = "Операции с общими настройками системы")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @ApiOperation(value = "Список")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.GET, value = "/setting/list")
    public ResponseEntity<ResponseServer> listSettings() {
        return ResponseServer.OK(true, settingService.find());
    }

    @ApiOperation(value = "Редактирование")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "string", paramType = "header"),
    })
    @RequestMapping(method = RequestMethod.POST, value = "/setting/edit")
    public ResponseEntity<ResponseServer> editSettings(@RequestBody List<SettingDto> settings) {
        settingService.editSettings(settings);
        return ResponseServer.OK(true, "edit was successful");
    }

}
