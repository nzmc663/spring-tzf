package com.gupaoedu.vip.spring.framework.annotation;

import java.lang.annotation.*;
/**
 * 请求地址映射注解
 * @author tzf
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
