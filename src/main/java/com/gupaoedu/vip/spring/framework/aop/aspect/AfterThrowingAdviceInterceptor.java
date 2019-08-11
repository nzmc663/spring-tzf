package com.gupaoedu.vip.spring.framework.aop.aspect;

import com.gupaoedu.vip.spring.framework.aop.intercept.MethodInterceptor;
import com.gupaoedu.vip.spring.framework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 异常通知
 *
 * @author tzf
 */
public class AfterThrowingAdviceInterceptor extends AbstractAspectAdvice implements Advice, MethodInterceptor {

    /**
     * 异常信息名称
     */
    private String throwingName;

    public AfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget,String throwingName) {
        super(aspectMethod, aspectTarget);
        this.throwingName = throwingName;
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.processed();
        }catch (Throwable throwable){
            invokeAdviceMethod(invocation,null,throwable.getCause());
            throw throwable;
        }
    }

    public void setThrowingName(String throwingName){
        this.throwingName = throwingName;
    }
}
