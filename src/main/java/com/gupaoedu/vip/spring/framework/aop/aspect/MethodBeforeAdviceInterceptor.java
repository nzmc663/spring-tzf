package com.gupaoedu.vip.spring.framework.aop.aspect;

import com.gupaoedu.vip.spring.framework.aop.intercept.MethodInterceptor;
import com.gupaoedu.vip.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 前置通知
 * @author tzf
 */
public class MethodBeforeAdviceInterceptor extends AbstractAspectAdvice implements Advice, MethodInterceptor {
    /**
     * 切入点
     */
    private JoinPoint joinPoint;

    public MethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.joinPoint = invocation;
        this.before(invocation.getMethod(),invocation.getArguments(),invocation.getThis());
        return invocation.processed();
    }

    private void before(Method method,Object arguments,Object target) throws Throwable{
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }
}
