package com.gupaoedu.vip.spring.framework.aop.intercept;

/**
 * 方法拦截器顶层接口
 *      拦截业务类执行方法，实现AOP织入
 *
 * @author tzf
 */
public interface MethodInterceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;

}
