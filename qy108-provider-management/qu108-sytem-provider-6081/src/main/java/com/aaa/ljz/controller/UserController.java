package com.aaa.ljz.controller;

import com.aaa.ljz.model.User;
import com.aaa.ljz.service.UserService;
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
public class UserController {
    @Autowired
    private UserService userService;

    /**
    *@Author: lijinzhe
    *@Data: 2020/5/28
    *@ruturn:
    *@Description: 这是添加用户的方法
    */
    @PostMapping("/addUser")
    public Boolean addUser(@RequestBody User user){
        try {
            userService.queryListByPage(user , 29 ,7);

        }catch (Exception e){
            e.printStackTrace();
        }
        return userService.addUser(user);
    }


}
