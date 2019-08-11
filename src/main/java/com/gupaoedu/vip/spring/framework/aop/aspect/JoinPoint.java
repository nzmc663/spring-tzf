package com.gupaoedu.vip.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 切入点顶层设计
 *
 * @author tzf
 */
public interface JoinPoint {

    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute(String key,Object value);

    Object getUserAttribute(String key);
}
