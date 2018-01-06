package com.yoku.arya.annotation;

import java.lang.annotation.*;

/**
 * @auth HODO
 * @data 2018/1/4
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcProvider {

    /**
     * provider name
     * @return
     */
    String value() default "";
}
