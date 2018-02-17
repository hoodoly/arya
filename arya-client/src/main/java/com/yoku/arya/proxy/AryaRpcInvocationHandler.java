package com.yoku.arya.proxy;


import com.yoku.arya.NettyClient;
import com.yoku.arya.RpcRequest;
import com.yoku.arya.RpcResponse;
import com.yoku.arya.zk.Registry;
import com.yoku.arya.zk.RegistryFactory;
import com.yoku.arya.zk.manager.ZookeeperRegistryManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**实际处理远程调用方法
 * @author HODO
 */
public class AryaRpcInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().contains("toString")) {
            method.invoke(proxy, args);
        }
        RpcRequest rpcRequest = new RpcRequest("121", method.getDeclaringClass(), method, args);
        Registry registry = RegistryFactory.getRegistry(ZookeeperRegistryManager.class);
        String serverAddr = registry.consumer(method.getDeclaringClass().getName());
        NettyClient nettyClient = new NettyClient(serverAddr);
        RpcResponse rpcResponse = nettyClient.invoker(rpcRequest);
        return rpcResponse.getObject();
    }


}
