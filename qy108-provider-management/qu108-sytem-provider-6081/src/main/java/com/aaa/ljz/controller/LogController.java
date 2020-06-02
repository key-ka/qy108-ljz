package com.aaa.ljz.controller;

import com.aaa.ljz.base.BaseSerivce;
import com.aaa.ljz.base.CommonController;
import com.aaa.ljz.base.ResultData;
import com.aaa.ljz.model.LoginLogs;
import com.aaa.ljz.service.LoginLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: lijinzhe
 * @Data: 2020/5/28
 * @Description:
 */
@RestController
public class LogController extends CommonController<LoginLogs> {
   @Autowired
   private LoginLogsService loginLogsService;


   @Override
    public BaseSerivce<LoginLogs> getBaseService() {
        return loginLogsService;
    }

    /**
    *@Author: lijinzhe
    *@Data: 2020/5/28
    *@ruturn:
    *@Description: 实现登录
    */
    @PostMapping("/addLoginLog")
    public ResultData addLoginLog(@RequestBody Map map){
        return super.add(map);
    }
}
