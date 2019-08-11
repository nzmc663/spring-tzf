package com.gupaoedu.vip.spring.framework.beans;

/**
 * Bean包装器(对外暴露包装后的bean)
 *
 * @author tzf
 */
public class BeanWrapper {

    private Object warppedInstance;

    private Class<?> warppedClass;

    public BeanWrapper(Object warppedInstance){
        this.warppedInstance = warppedInstance;
    }

    public Object getWarppedInstance() {
        return this.warppedInstance;
    }

    public Class<?> getWarppedClass() {
        // 返回代理以后的Class
        // 可能会是这个 $Proxy0
        return this.warppedInstance.getClass();
    }

}
