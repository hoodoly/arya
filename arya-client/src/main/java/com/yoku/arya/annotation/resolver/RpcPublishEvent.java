package com.yoku.arya.annotation.resolver;

import org.springframework.context.ApplicationEvent;

/**
 * @author HODO
 * @date 2018/1/6
 */
public class RpcPublishEvent extends ApplicationEvent{

    private String beanName;

    private Object serviceBean;

    public RpcPublishEvent(Object source) {
        super(source);
    }
}
