package com.gupaoedu.vip.spring.framework.aop.aspect;

import com.gupaoedu.vip.spring.framework.aop.intercept.MethodInterceptor;
import com.gupaoedu.vip.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 后置通知
 *
 * @author tzf
 */
public class AfterReturningAdviceInterceptor extends AbstractAspectAdvice implements Advice, MethodInterceptor {
    /**
     * 切入点
     */
    private JoinPoint joinPoint;

    public AfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returnVal = invocation.processed();
        this.joinPoint = invocation;
        this.afterReturning(returnVal,invocation.getMethod(),invocation.getArguments(),invocation.getThis());
        return returnVal;
    }

    private void afterReturning(Object returnVal, Method method,Object[] arguments,Object currentObject) throws Throwable{
        super.invokeAdviceMethod(this.joinPoint,returnVal,null);
    }
}
