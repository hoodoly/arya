package com.yoku.arya.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author HODO
 */
public class AryaRpcProxyFactory implements RpcProxyFactory {


    @Override
    @SuppressWarnings("unchecked")
    public <T> T createProxyBean(Class<T> rpcServiceInterface) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{rpcServiceInterface}, new AryaRpcInvocationHandler());
    }
}
