package com.yoku.arya.client;


/**
 * @author xhj
 *
 */
public interface RpcClient {

    /**
     * 通过代理对象调用远程接口
     * @return 代理对象
     */
    <T> T refer(Class<T> rpcServiceInterface);
}
