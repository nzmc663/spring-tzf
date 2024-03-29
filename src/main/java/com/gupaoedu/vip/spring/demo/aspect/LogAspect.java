package com.gupaoedu.vip.spring.demo.aspect;

import com.gupaoedu.vip.spring.framework.aop.aspect.JoinPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 日志切面
 *
 * @author tzf
 */
@Slf4j
public class LogAspect {

    //在调用一个方法之前，执行before方法
    public void before(JoinPoint joinPoint){
        joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(),System.currentTimeMillis());
        //这个方法中的逻辑，是由我们自己写的
        log.info("Invoker Before Method!!!" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    //在调用一个方法之后，执行after方法
    public void after(JoinPoint joinPoint){
        log.info("Invoker After Method!!!" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.println("use time :" + (endTime - startTime));
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable ex){
        log.info("出现异常" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()) +
                "\nThrows:" + ex.getMessage());
    }


    //环绕通知
    public void round(JoinPoint joinPoint){
        log.info("Invoker Round Method!!!" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

}
