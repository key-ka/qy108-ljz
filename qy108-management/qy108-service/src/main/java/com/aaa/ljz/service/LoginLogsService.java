package com.aaa.ljz.service;

import com.aaa.ljz.base.BaseSerivce;
import com.aaa.ljz.mapper.LoginLogsMapper;
import com.aaa.ljz.model.LoginLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LoginLogsService extends BaseSerivce<LoginLogs> {

    @Autowired
    private LoginLogsMapper loginLogsMapper;

    public int doLoginLogs(String username) throws UnknownHostException {
        LoginLogs loginLogs = new LoginLogs();
        //获取ip地址
        InetAddress location = InetAddress.getLocalHost();
        String ip=location.getHostAddress();
        //获取登录地址
        String address = String.valueOf(InetAddress.getLoopbackAddress());
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());

        loginLogs.setIp(ip).setUsername(username).setLoginTime(time).setLocation(address);
        return loginLogsMapper.insert(loginLogs);
    }
}
