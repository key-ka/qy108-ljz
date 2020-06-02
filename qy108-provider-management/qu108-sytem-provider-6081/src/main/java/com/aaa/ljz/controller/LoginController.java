package com.aaa.ljz.controller;

import com.aaa.ljz.model.User;
import com.aaa.ljz.redis.RedisService;
import com.aaa.ljz.service.LoginLogsService;
import com.aaa.ljz.service.LoginService;
import com.aaa.ljz.vo.TokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;


@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LoginLogsService loginLogsService;


    /**
     * @author Seven Lee
     * @description
     *      执行登录操作
     * @param [user]
     * @date 2020/5/15
     * @return com.aaa.lee.base.ResultData
     * @throws
    **/

    @PostMapping("/doLogin")
    public TokenVo doLogin(@RequestBody User user){
        TokenVo tokenVo = loginService.doLogin(user, redisService);
        try {
            if ("true".equals(tokenVo.getIfSuccess().toString())){
                loginLogsService.doLoginLogs(user.getUsername());
            }
        }catch (UnknownHostException u){
            u.printStackTrace();
            System.out.println("系统正在维护，请稍后再试");
        }
        return tokenVo;
    }
}