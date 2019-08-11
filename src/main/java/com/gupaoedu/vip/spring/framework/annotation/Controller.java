package com.gupaoedu.vip.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 前端控制层
 * @author tzf
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
