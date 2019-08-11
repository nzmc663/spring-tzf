package com.gupaoedu.vip.spring.framework.beans.config;

/**
 * bean初始化后置处理器
 * @author tzf
 */
public class BeanPostProcessor {
    /**
     * bean初始化之前扩展点
     */
    public Object postProcessBeforeInitialization(Object bean,String beanName) throws Exception{
        return bean;
    }
    /**
     * bean初始化之后扩展点
     */
    public Object postProcessAfterInitialization(Object bean,String beanName) throws Exception{
        return bean;
    }
}
