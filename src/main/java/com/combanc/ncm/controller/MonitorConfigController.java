package com.combanc.ncm.controller;

import com.combanc.ncm.entity.MonitorConfigTemplate;
import com.combanc.ncm.entity.SysUser;
import com.combanc.ncm.service.MonitorConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "配置接口", tags = {"config"})
@RequestMapping(value = "/config/")
public class MonitorConfigController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MonitorConfigService monitorConfigService;

    private String msg = "成功";

    private String code = "1";

    @ApiOperation("发送配置请求")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "ids", value = "设备ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "tempId", value = "模板ID", required = true, dataType = "String"),
    })
    @PostMapping("sendConfig")
    @ResponseBody
    public String sendConfig(String ids, Long tempId, HttpServletRequest request) {
        String hostIp = request.getRemoteAddr();
        SysUser user = (SysUser)request.getSession().getAttribute("username");
        monitorConfigService.sendConfig(ids, tempId, hostIp, user.getUsername());
        return "success";
    }

}
