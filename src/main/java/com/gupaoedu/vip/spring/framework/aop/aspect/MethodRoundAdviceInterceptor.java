package com.gupaoedu.vip.spring.framework.aop.aspect;

import com.gupaoedu.vip.spring.framework.aop.intercept.MethodInterceptor;
import com.gupaoedu.vip.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 环绕通知
 *
 * @author tzf
 */
public class MethodRoundAdviceInterceptor extends AbstractAspectAdvice implements Advice, MethodInterceptor {
    /**
     * 切入点
     */
    private JoinPoint joinPoint;

    public MethodRoundAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.joinPoint = invocation;
        this.round(invocation.getMethod(),invocation.getArguments(),invocation.getThis());
        Object returnValue = invocation.processed();
        this.round(invocation.getMethod(),invocation.getArguments(),invocation.getThis());
        return returnValue;
    }

    private void round(Method method,Object arguments,Object target) throws Throwable{
        super.invokeAdviceMethod(this.joinPoint,null,null);
    }
}
