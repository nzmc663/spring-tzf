package com.gupaoedu.vip.spring.framework.beans.support;

import com.gupaoedu.vip.spring.framework.beans.config.BeanDefinition;
import com.gupaoedu.vip.spring.framework.context.support.AbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认beanFactory的实现
 *
 * @author tzf
 */
public class DefaultListableBeanFactory extends AbstractApplicationContext {
    /**
     * 存储注册配置的BeanDefinition,伪IOC容器的实现
     */
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

}
