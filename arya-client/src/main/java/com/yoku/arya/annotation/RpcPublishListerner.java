package com.yoku.arya.annotation;

import org.springframework.context.ApplicationListener;

/**
 * @author HODO
 * @date 2018/1/6
 */
public class RpcPublishListerner implements ApplicationListener<RpcPublishEvent> {
    @Override
    public void onApplicationEvent(RpcPublishEvent rpcPublishEvent) {

    }
}
