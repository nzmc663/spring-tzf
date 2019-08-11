package com.gupaoedu.vip.spring.framework.aop.config;

/**
 * 解析AOP的配置类
 *
 * @author tzf
 */
public class AopConfig {
    /**
     * 切入点规则
     */
    private String pointCut;
    /**
     * 前置通知执行方法名称
     */
    private String aspectBefore;
    /**
     * 后置通知执行方法名称
     */
    private String aspectAfter;
    /**
     * 环绕通知执行方法名称
     */
    private String aspectRound;
    /**
     * 切面类
     */
    private String aspectClass;
    /**
     * 异常通知执行方法名称
     */
    private String aspectAfterThrow;
    /**
     * 异常名称信息
     */
    private String aspectAfterThrowingName;

    public String getPointCut() {
        return pointCut;
    }

    public void setPointCut(String pointCut) {
        this.pointCut = pointCut;
    }

    public String getAspectBefore() {
        return aspectBefore;
    }

    public void setAspectBefore(String aspectBefore) {
        this.aspectBefore = aspectBefore;
    }

    public String getAspectAfter() {
        return aspectAfter;
    }

    public void setAspectAfter(String aspectAfter) {
        this.aspectAfter = aspectAfter;
    }

    public String getAspectRound() { return aspectRound; }

    public void setAspectRound(String aspectRound) { this.aspectRound = aspectRound; }

    public String getAspectClass() {
        return aspectClass;
    }

    public void setAspectClass(String aspectClass) {
        this.aspectClass = aspectClass;
    }

    public String getAspectAfterThrow() {
        return aspectAfterThrow;
    }

    public void setAspectAfterThrow(String aspectAfterThrow) {
        this.aspectAfterThrow = aspectAfterThrow;
    }

    public String getAspectAfterThrowingName() {
        return aspectAfterThrowingName;
    }

    public void setAspectAfterThrowingName(String aspectAfterThrowingName) {
        this.aspectAfterThrowingName = aspectAfterThrowingName;
    }
}
