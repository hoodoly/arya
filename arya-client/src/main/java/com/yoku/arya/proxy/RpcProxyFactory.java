package com.yoku.arya.proxy;

/**
 * @author xiahj@dxy.cn
 */
public interface RpcProxyFactory {

    /**
     * 给定Rpc service 获取对应代理对象
     * @param rpcServiceInterface service interface
     * @return proxy bean
     */
    <T> T createProxyBean(Class<T> rpcServiceInterface);
}
