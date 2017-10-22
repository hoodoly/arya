package com.yoku.arya.proxy;



import com.yoku.arya.NettyClient;
import com.yoku.arya.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**实际处理远程调用方法
 * @author HODO
 */
public class AryaRpcInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest("121", proxy.getClass(), method, args);
        NettyClient nettyClient = new NettyClient("127.0.0.1", 12133);
        return nettyClient.invoker(rpcRequest);
    }
}
