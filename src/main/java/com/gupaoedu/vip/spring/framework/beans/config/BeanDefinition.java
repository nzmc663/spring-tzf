package com.gupaoedu.vip.spring.framework.beans.config;

/**
 * 配置文件解析bean定义实体
 *
 * @author tzf
 */
public class BeanDefinition {
    /**
     * bean class 全限定名
     */
    private String beanClassName;
    /**
     * 是否需要懒加载 默认false
     */
    private boolean lazyInit = false;
    /**
     * bean 对应生产工厂全限定名
     */
    private String factoryBeanName;
    /**
     * 是否单例 默认true
     */
    private boolean isSingleton = true;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean singleton) {
        isSingleton = singleton;
    }
}
