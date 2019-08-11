package com.gupaoedu.vip.spring.framework.aop;

/**
 * AOP 代理顶层接口
 *
 * @author tzf
 */
public interface AopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
