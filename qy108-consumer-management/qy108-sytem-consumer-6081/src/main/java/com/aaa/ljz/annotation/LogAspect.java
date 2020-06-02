package com.aaa.ljz.annotation;

import com.aaa.ljz.model.User;
import com.aaa.ljz.service.IQYService;
import com.aaa.ljz.utils.DateUtils;
import com.aaa.ljz.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.aaa.ljz.staticstatus.TimeProperties.TIME_TYPE;

/**
 * @Author: lijinzhe
 * @Data: 2020/5/28
 * @Description:
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    @Autowired
    private IQYService iqyService;
    /**
    *@Author: lijinzhe
    *@Data: 2020/5/28
    *@Description:
     * 定义一个切面
     * 也就是说当检测到这个注解存在的时候，aop才会生效
    */
    @Pointcut("@annotation(com.aaa.ljz.annotation.LoginLogAnnotation)")
    public void pointcut(){
        //shenmedoubuyongxie
    }

    /**
    *@Author: lijinzhe
    *@Data: 2020/5/28
    *@ruturn:
    *@Description: 定义环形切点
     *           ProceedingJoinPoint:
     *           里面封装了目标路径中的所有参数
     *           可以任意获取
    */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws ClassNotFoundException {

        //fanhuihzhiliexing
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 2.获取username信息,其实username信息在方法的参数中，也就是说只要获取到了目标方法的参数就能拿到username的值
    Object[] args = proceedingJoinPoint.getArgs();
        User user = null;
        for (Object arg : args){
            user =(User) arg;
        }
        System.out.println(user.getUsername());
        // 3.获取loginTime
        // 会使用到一个新的jar包，这个jar包是时间转换的一个工具类
    String dateTime = DateUtils.formatDate(new Date(), TIME_TYPE);

        // 4.获取ip地址
        // 先得获取Request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        System.out.println(request);
        String ip = IPUtils.getIpAddr(request);
        // 5.获取opeationType和operationName
        String className =proceedingJoinPoint.getTarget().getClass().getName();
        String targetMethodName = proceedingJoinPoint.getSignature().getName();
        Class targetClass = Class.forName(className);
        Method[] methods =targetClass.getMethods();
        String operationName = "";
        String operationType = "";
        for (Method method  : methods){
            String methodName = method.getName();
            if (methodName.equals(targetMethodName)){
                Class[] paramenterTypes = method.getParameterTypes();
                if (paramenterTypes.length ==  args.length){
                    operationName  = method.getAnnotation(LoginLogAnnotation.class).operationName();
                    operationType = method.getAnnotation(LoginLogAnnotation.class).operationType();
                }
            }
        }
        Map map  =new HashMap();
        map.put("username", user.getUsername());
        map.put("loginTime", dateTime);
        map.put("ip", ip);
        map.put("location","没有");
        map.put("operationType", operationType);
        map.put("operationName", operationName);

        iqyService.addLoginLog(map);

        return result;
    }
}
