package com.gupaoedu.vip.spring.framework.webmvc.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author tzf
 */
public class HandlerMapping {
    /**
     * 保存方法对应的实例
     */
    private Object controller;
    /**
     * 保存映射的方法
     */
    private Method method;
    /**
     * URL的正则匹配
     */
    private Pattern pattern;

    public HandlerMapping(Pattern pattern, Object controller, Method method) {
        this.pattern = pattern;
        this.controller = controller;
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
