package com.yoku.arya.annotation;

import java.lang.annotation.*;

/**
 * @author  HODO
 * @date  2018/1/4
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcConsumer {

    String value() default "";
}
