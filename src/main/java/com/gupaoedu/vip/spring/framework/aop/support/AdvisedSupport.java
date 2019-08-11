package com.gupaoedu.vip.spring.framework.aop.support;

import com.gupaoedu.vip.spring.framework.aop.aspect.AfterReturningAdviceInterceptor;
import com.gupaoedu.vip.spring.framework.aop.aspect.AfterThrowingAdviceInterceptor;
import com.gupaoedu.vip.spring.framework.aop.aspect.MethodBeforeAdviceInterceptor;
import com.gupaoedu.vip.spring.framework.aop.aspect.MethodRoundAdviceInterceptor;
import com.gupaoedu.vip.spring.framework.aop.config.AopConfig;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 切面通知(与切面类一一对应)
 *
 * @author tzf
 */
public class AdvisedSupport {
    /**
     * 被代理class对象
     */
    private Class<?> targetClass;
    /**
     * 被代理对象实例
     */
    private Object target;
    /**
     * 对应AOP配置
     */
    private AopConfig config;
    /**
     * 正则匹配器(利用正则判断配置切面是否拦截)
     */
    private Pattern pointCutClassPattern;
    /**
     * 方法缓存
     */
    private transient Map<Method, List<Object>> methodCache;

    public AdvisedSupport(AopConfig config) {
        this.config = config;
    }

    public List<Object> getInterceptorAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> cached = methodCache.get(method);
        if (cached == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCache.get(m);
            this.methodCache.put(m, cached);
        }
        return cached;
    }
    /**
     * setter方法
     */
    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parse();
    }
    /**
     * 解析被代理类
     */
    private void parse() {
        //pointCut = public .* com.gupaoedu.vip.spring.demo.service..*Service..*(.*)
        String pointCut = config.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        //正则操作
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        this.pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ") + 1));
        this.methodCache = new HashMap<>();
        try {
            Pattern pattern = Pattern.compile(pointCut);

            Class aspectClass = Class.forName(this.config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();
            for (Method m : aspectClass.getMethods()) {
                aspectMethods.put(m.getName(), m);
            }
            for (Method m : this.targetClass.getMethods()) {
                String methodString = m.toString();
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws"));
                }
                Matcher matcher = pattern.matcher(methodString);
                if (matcher.matches()) {
                    //执行器链
                    List<Object> advices = new LinkedList<>();
                    //把每一个方法包装成 MethodInterceptor
                    //round
                    if (StringUtils.isNotBlank(this.config.getAspectRound())) {
                        advices.add(new MethodRoundAdviceInterceptor(aspectMethods.get(this.config.getAspectRound()), aspectClass.newInstance()));
                    }
                    //before
                    if (StringUtils.isNotBlank(this.config.getAspectBefore())) {
                        advices.add(new MethodBeforeAdviceInterceptor(aspectMethods.get(this.config.getAspectBefore()), aspectClass.newInstance()));
                    }
                    //after
                    if (StringUtils.isNotBlank(this.config.getAspectAfter())) {
                        advices.add(new AfterReturningAdviceInterceptor(aspectMethods.get(this.config.getAspectAfter()), aspectClass.newInstance()));
                    }
                    //throwing
                    if (StringUtils.isNotBlank(this.config.getAspectAfterThrow())) {
                        advices.add(new AfterThrowingAdviceInterceptor(aspectMethods.get(this.config.getAspectAfter()), aspectClass.newInstance(), this.config.getAspectAfterThrowingName()));
                    }
                    methodCache.put(m, advices);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean pointCutMatch() {
        return this.pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public Object getTarget() {
        return this.target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
