package com.aaa.ljz.service;

import com.aaa.ljz.base.BaseSerivce;
import com.aaa.ljz.mapper.UserMapper;
import com.aaa.ljz.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: lijinzhe
 * @Data: 2020/5/28
 * @Description:
 */
public class UserService  extends BaseSerivce<User>{
    @Autowired
    private UserMapper userMapper;

    /**
    *@Author: lijinzhe
    *@Data: 2020/5/28 11:22
    *@ruturn:
    *@Description: 这个方法用来添加用户的 参数user是前端传来的
    */
    public Boolean addUser(User user){
        Date date =new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        String format = simpleDateFormat.format(date);
        User user1 = user.setUsername(user.getUsername()).setDeptId(user.getDeptId()).setStatus(user.getStatus());
        if ("".equals(user1)&&user1==null){
            return false;
        }else {
            int insert = userMapper.insert(user1);
            if (insert>0){
                return  true;
            }

             else {
                return false;
            }
            }

    }
}
