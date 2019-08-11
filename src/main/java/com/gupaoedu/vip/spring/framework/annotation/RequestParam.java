package com.gupaoedu.vip.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 参数映射注解
 * @author tzf
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value() default "";

    boolean required() default true;

}
