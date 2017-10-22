package com.yoku.arya.client;

import com.yoku.arya.proxy.AryaRpcProxyFactory;
import com.yoku.arya.proxy.RpcProxyFactory;

public class AryaRpcClient implements RpcClient {

    AryaRpcProxyFactory aryaRpcProxyFactory;

    @Override
    public <T> T refer(Class<T> rpcServiceInterface) {
        return (T) aryaRpcProxyFactory.createProxyBean(rpcServiceInterface);
    }
}
