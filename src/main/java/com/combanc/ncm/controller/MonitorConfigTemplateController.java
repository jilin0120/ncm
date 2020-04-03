package com.combanc.ncm.controller;

import com.combanc.ncm.entity.MonitorConfigTemplate;
import com.combanc.ncm.service.MonitorConfigTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "配置模板接口", tags = {"configTemplate"})
@RequestMapping(value = "/configTemplate/")
public class MonitorConfigTemplateController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MonitorConfigTemplateService monitorConfigTemplateService;

    private String msg = "成功";

    private String code = "1";

    @ApiOperation(value = "配置模板列表", notes = "分页查询配置模板列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNo", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "orderBy", value = "排序", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键字", required = false, dataType = "String")
    })
    @GetMapping(value = "list")
    @ResponseBody
    public Page<MonitorConfigTemplate> findPageList(Integer pageNo, Integer pageSize, String orderBy, String keyword) {
        Page<MonitorConfigTemplate> pageInfo = monitorConfigTemplateService.findList(pageNo, pageSize, orderBy, keyword);
        return pageInfo;
    }

    @ApiOperation(value = "获取模板详细信息", notes = "根据id来获取模板详细信息")
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "query")
    @GetMapping(value = "getById")
    @ResponseBody
    public Map getById(@RequestParam Long id) {
        Map m = new HashMap();
        try {
            MonitorConfigTemplate mct = monitorConfigTemplateService.get(id);
            m.put("data", mct);

        } catch (Exception e) {
            code = "-1";
            e.printStackTrace();
        }
        m.put("code", code);
        return m;
    }

    @ApiOperation("新增")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "command", value = "指令", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "note", value = "描述", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isUse", value = "是否使用", required = true,  dataType = "String"),
    })
    @PostMapping("add")
    @ResponseBody
    public Long add(MonitorConfigTemplate mct) {
        MonitorConfigTemplate obj =  monitorConfigTemplateService.save(mct);
        return obj.getId();
    }

    @ApiOperation("修改")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "command", value = "指令", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "note", value = "描述", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isUse", value = "是否使用", required = true,  dataType = "String")
    })
    @PostMapping("update")
    @ResponseBody
    public Long update(MonitorConfigTemplate mct) {
        MonitorConfigTemplate obj =  monitorConfigTemplateService.save(mct);
        return obj.getId();
    }


    @ApiOperation("删除")
    @ApiImplicitParam(paramType = "query", name = "id", value = "id", required = true, dataType = "Long")
    @DeleteMapping("delete")
    @ResponseBody
    public int delete(Long id) {
        monitorConfigTemplateService.delete(id);
        return 1;
    }

    @ApiOperation("批量删除")
    @ApiImplicitParam(paramType = "query", name = "ids", value = "ids", required = true, dataType = "String")
    @PostMapping("batchDelete")
    @ResponseBody
    public int batchDelete(String ids) {
        String[] str = ids.split(",");
        Long[] idStr = new Long[str.length];
        for(int i=0;i<str.length;i++){
            idStr[i] = Long.parseLong(str[i]);
        }
        monitorConfigTemplateService.deletes(idStr);
        return str.length;
    }

}
