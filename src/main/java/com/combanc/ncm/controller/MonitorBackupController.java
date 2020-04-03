package com.combanc.ncm.controller;

import com.combanc.ncm.entity.MonitorBackupFile;
import com.combanc.ncm.entity.MonitorConfigTemplate;
import com.combanc.ncm.service.MonitorBackupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "配置备份接口", tags = {"backup"})
@RequestMapping(value = "/backup/")
public class MonitorBackupController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MonitorBackupService monitorBackupService;

    private String msg = "成功";

    private String code = "1";

    @ApiOperation(value = "配置备份列表", notes = "分页查询配置备份列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNo", value = "页码", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "orderBy", value = "排序", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键字", required = false, dataType = "String")
    })
    @GetMapping(value = "list")
    @ResponseBody
    public Page<MonitorBackupFile> findPageList(Integer pageNo, Integer pageSize, String orderBy, String keyword, String startTime, String endTime) {
        pageNo -= 1;
        Page<MonitorBackupFile> pageInfo = monitorBackupService.findList(pageNo, pageSize, orderBy, keyword, startTime, endTime);
        return pageInfo;
    }


    @ApiOperation("删除")
    @ApiImplicitParam(paramType = "query", name = "id", value = "id", required = true, dataType = "Long")
    @PostMapping("delete")
    @ResponseBody
    public int delete(Long id) {
        monitorBackupService.delete(id);
        return 1;
    }

    @ApiOperation("批量删除")
    @ApiImplicitParam(paramType = "query", name = "ids", value = "ids", required = true, dataType = "String")
    @PostMapping("batchDelete")
    @ResponseBody
    public Map batchDelete(String ids) {
        Map m = new HashMap();
        String[] str = ids.split(",");
        Long[] idStr = new Long[str.length];
        for(int i=0;i<str.length;i++){
            idStr[i] = Long.parseLong(str[i]);
        }
        monitorBackupService.deletes(idStr);
        m.put("code", code);
        m.put("msg", msg);
        return m;
    }

    @ApiOperation("手动备份")
    @PostMapping("backup")
    @ResponseBody
    public int backup(Long id) {
        monitorBackupService.startBackup();
        return 1;
    }

    @RequestMapping(value = { "/downloads" })
    public void downloads(Long id, HttpServletResponse response) {
       try {
           MonitorBackupFile backupFile = monitorBackupService.getById(id);
           String path = backupFile.getLocation();
           File file = new File(path);// path是根据日志路径和文件名拼接出来的
           String filename = file.getName();// 获取日志文件名称
           InputStream fis = new BufferedInputStream(new FileInputStream(path));
           byte[] buffer = new byte[fis.available()];
           fis.read(buffer);
           fis.close();
           response.reset();
           response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1"));
           response.addHeader("Content-Length", "" + file.length());
           OutputStream os = new BufferedOutputStream(response.getOutputStream());
           response.setContentType("application/octet-stream");
           os.write(buffer);// 输出文件
           os.flush();
           os.close();
       }catch(Exception e){
           e.printStackTrace();
       }
    }

}
