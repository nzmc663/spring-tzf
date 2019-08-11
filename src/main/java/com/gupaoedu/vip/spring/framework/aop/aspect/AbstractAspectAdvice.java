package com.gupaoedu.vip.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 切面通知
 *
 * @author tzf
 */
public class AbstractAspectAdvice implements Advice {
    /**
     * 切面方法
     */
    private Method aspectMethod;
    /**
     * 切面反射执行对象
     */
    private Object aspectTarget;

    public AbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviceMethod(JoinPoint joinPoint, Object returnValue, Throwable throwable) throws Throwable {
        Class<?>[] paramTypes = this.aspectMethod.getParameterTypes();
        if (null == paramTypes || paramTypes.length == 0) {
            return this.aspectMethod.invoke(aspectTarget);
        } else {
            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] == JoinPoint.class){
                    args[i] = joinPoint;
                }else if (paramTypes[i] == Throwable.class){
                    args[i] = throwable;
                }else if (paramTypes[i] == Object.class){
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget,args);
        }
    }
}
