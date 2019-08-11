package com.gupaoedu.vip.spring.framework.aop;

import com.gupaoedu.vip.spring.framework.aop.support.AdvisedSupport;

/**
 * cglib动态代理实现 TODO 未实现
 *
 * @author tzf
 */
public class CglibAopProxy implements AopProxy {

    public CglibAopProxy(AdvisedSupport config){

    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
