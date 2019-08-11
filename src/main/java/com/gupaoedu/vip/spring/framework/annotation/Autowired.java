package com.gupaoedu.vip.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 自动注入
 * @author tzf
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
