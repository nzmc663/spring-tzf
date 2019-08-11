package com.gupaoedu.vip.spring.framework.webmvc.servlet;

import com.gupaoedu.vip.spring.framework.annotation.RequestParam;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tzf
 */
public class HandlerAdapter {

    public boolean support(Object handler) {
        return (handler instanceof HandlerMapping);
    }

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMapping handlerMapping = (HandlerMapping) handler;

        //把方法的形参列表和request的参数列表所在顺序进行一一对应
        Map<String, Integer> paramIndexMapping = new HashMap<>();

        //提取方法中加了注解的参数
        //把方法上的注解拿到，得到的是一个二维数组
        //因为一个参数可以有多个注解，而一个方法又有多个参数
        Annotation[][] annotations = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]){
                if (annotation instanceof RequestParam){
                    String paramName = ((RequestParam) annotation).value();
                    if (StringUtils.isNotBlank(paramName)){
                        paramIndexMapping.put(paramName,i);
                    }
                }
            }
        }

        //提取方法中的request和response参数
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++){
            Class<?> type = paramTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class){
                paramIndexMapping.put(type.getName(),i);
            }
        }

        //获得方法的形参列表
        Map<String,String[]> params = request.getParameterMap();

        //实参列表
        Object[] paramValues = new Object[paramTypes.length];

        for (Map.Entry<String,String[]> param : params.entrySet()){
            String value = Arrays.toString(param.getValue()).replaceAll("\\[\\]","").replaceAll("\\s",",");
            if (!paramIndexMapping.containsKey(param.getKey())) continue;

            int index = paramIndexMapping.get(param.getKey());
            paramValues[index] = caseStringValue(value,paramTypes[index]);
        }

        if (paramIndexMapping.containsKey(HttpServletRequest.class.getName())){
            int index = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = request;
        }

        if (paramIndexMapping.containsKey(HttpServletResponse.class.getName())){
            int index = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = response;
        }

        Object result = handlerMapping.getMethod().invoke(((HandlerMapping) handler).getController(),paramValues);
        if (result == null || result instanceof Void) return null;
        return (handlerMapping.getMethod().getReturnType() == ModelAndView.class)? (ModelAndView) result : null;
    }

    private Object caseStringValue(String value, Class<?> paramType) {
        if (String.class == paramType){
            return value;
        }
        if (Integer.class == paramType){
            return Integer.valueOf(value);
        }else if (Double.class == paramType){
            return Double.valueOf(value);
        }else {
            return value;
        }
        //如果还有double或者其他类型，继续加if
        //这时候，我们应该想到策略模式了
        //在这里暂时不实现，希望小伙伴自己来实现
    }
}
