package com.aaa.ljz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2020/5/12 15:56
 * @Description
 *      也可以使用@SpringCloudApplication(新注解)，代替
 *          @SpringBootApplication, @EnableDiscoveryClient, @EnableCircuitBreaker
 **/
@SpringBootApplication
@MapperScan("com.aaa.lee.mapper")
@EnableDiscoveryClient
@EnableCircuitBreaker
public class ApplicationRun8081 {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRun8081.class, args);
    }

}
