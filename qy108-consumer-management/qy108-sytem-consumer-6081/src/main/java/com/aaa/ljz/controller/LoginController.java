package com.aaa.ljz.controller;

import com.aaa.ljz.base.BaseController;
import com.aaa.ljz.base.ResultData;
import com.aaa.ljz.model.User;
import com.aaa.ljz.service.IQYService;
import com.aaa.ljz.vo.TokenVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "登录信息", tags = "用户登录接口")
public class LoginController extends BaseController {

    @Autowired
    private IQYService iqyService;

    /**
     * @Summary:
     * @Author:  ljz
     * @description
     *          执行登录操作
     * @Data: 2020/5/16 12:29
     * @param user
     * @Return:com.aaa.ljz.base.ResultData
     */
    @PostMapping("/doLogin")
    @ApiOperation(value = "登录功能", notes = "用户执行登录功能")
    public ResultData doLogin(User user) {
        TokenVo tokenVo = iqyService.doLogin(user);
        if(tokenVo.getIfSuccess()) {
            return super.loginSuccess(tokenVo.getToken());
        }
        return super.loginFailed();
    }
}
