package com.gupaoedu.vip.spring.framework.core;

/**
 * bean工厂的顶层接口
 * @author tzf
 */
public interface BeanFactory {
    /**
     * 根据名称获取实例
     */
    Object getBean(String beanName) throws Exception;
    /**
     * 根据类型获取实例
     */
    Object getBean(Class<?> beanClass) throws Exception;
}
