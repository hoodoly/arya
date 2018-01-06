package com.yoku.arya.client;

import com.yoku.arya.proxy.AryaRpcProxyFactory;
import com.yoku.arya.proxy.RpcProxyFactory;
import com.yoku.arya.service.DemoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HODO
 */

@Slf4j
public class AryaRpcClient implements RpcClient {


    @Override
    public <T> T refer(Class<T> rpcServiceInterface) {
        return AryaRpcProxyFactory.getProxyFactory().createProxyBean(rpcServiceInterface);
    }
}
