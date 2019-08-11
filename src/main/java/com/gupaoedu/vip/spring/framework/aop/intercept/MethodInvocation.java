package com.gupaoedu.vip.spring.framework.aop.intercept;

import com.gupaoedu.vip.spring.framework.aop.aspect.JoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法代理类
 *
 * @author tzf
 */
public class MethodInvocation implements JoinPoint {
    /**
     * 代理类
     */
    private Object proxy;
    /**
     * 代理方法
     */
    private Method method;
    /**
     * 被代理实例
     */
    private Object target;
    /**
     * 参数
     */
    private Object[] arguments;
    /**
     * 拦截器链
     */
    private List<Object> interceptorsAndDynamicMethodMatchers;
    /**
     * 被代理Class对象
     */
    private Class<?> targetClass;
    /**
     * 方法拦截时可操作参数存放集合
     */
    private Map<String, Object> userAttributes;
    /**
     * 定义一个索引,从-1开始来记录当前拦截器执行的位置
     */
    private int currentInterceptorIndex = -1;

    public MethodInvocation(Object proxy,
                            Object target,
                            Method method,
                            Object[] arguments,
                            Class<?> targetClass,
                            List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.targetClass = targetClass;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }
    /**
     * 递归执行AOP代理类链(责任链模式)
     */
    public Object processed() throws Throwable {
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return this.method.invoke(this.target, this.arguments);
        }
        Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);

        //匹配joinPonit
        if (interceptorOrInterceptionAdvice instanceof MethodInterceptor) {
            MethodInterceptor methodInterceptor = (MethodInterceptor) interceptorOrInterceptionAdvice;
            return methodInterceptor.invoke(this);
        } else {
            //动态匹配失败时,略过当前Interceptor,调用下一个Interceptor
            return processed();
        }
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (value != null) {
            if (this.userAttributes == null) {
                this.userAttributes = new HashMap<String, Object>();
            }
            this.userAttributes.put(key, value);
        } else {
            if (this.userAttributes != null) {
                this.userAttributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return (this.userAttributes != null) ? this.userAttributes.get(key) : null;
    }
}
