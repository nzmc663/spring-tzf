package com.gupaoedu.vip.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Service 业务层自动注入bean
 * @author tzf
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
