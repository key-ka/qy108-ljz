package com.aaa.ljz.controller;

import com.aaa.ljz.base.BaseController;
import com.aaa.ljz.base.ResultData;
import com.aaa.ljz.model.User;
import com.aaa.ljz.service.IQYService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lijinzhe
 * @Data: 2020/5/28
 * @Description:
 */
@RestController
@Api(value = "yonghu",tags = "yonghujiekou")
public class UserController extends BaseController {

@Autowired
    private IQYService iqyService;
@PostMapping("/addUser")
    public ResultData addUser(@RequestBody User user){
    if (iqyService.addUser(user)){
        return loginSuccess();
    }else {
        return loginFailed();
    }
}




}
