package com.yoku.arya.client;

import com.yoku.arya.proxy.AryaRpcProxyFactory;
import com.yoku.arya.proxy.RpcProxyFactory;
import com.yoku.arya.service.DemoServiceImpl;
import com.yoku.arya.service.DemoSrevice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AryaRpcClient implements RpcClient {

    RpcProxyFactory rpcProxyFactory = new AryaRpcProxyFactory();

    @Override
    public <T> T refer(Class<T> rpcServiceInterface) {
        return rpcProxyFactory.createProxyBean(rpcServiceInterface);
    }

    public static void main(String[] args) {
        AryaRpcClient aryaRpcClient = new AryaRpcClient();
        log.error("12131");

        DemoSrevice demoSrevice = aryaRpcClient.refer(DemoSrevice.class);
        System.out.println("===========" + demoSrevice.count(123));
    }
}
