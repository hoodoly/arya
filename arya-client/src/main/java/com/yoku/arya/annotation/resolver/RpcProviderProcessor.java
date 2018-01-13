package com.yoku.arya.annotation.resolver;


import com.yoku.arya.NettyServer;
import com.yoku.arya.annotation.RpcProvider;
import com.yoku.arya.zk.Registry;
import com.yoku.arya.zk.RegistryFactory;
import com.yoku.arya.zk.manager.ZookeeperRegistryManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.SocketUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author HODO
 * @date 2018/1/4
 */
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
public class RpcProviderProcessor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    Registry registry = RegistryFactory.getRegistry(ZookeeperRegistryManager.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    protected void publishService() {
        Map<String, Object> provideServiceMap = applicationContext.getBeansWithAnnotation(RpcProvider.class);
        for (Map.Entry<String, Object> entry : provideServiceMap.entrySet()){
            publish(entry.getValue().getClass().getInterfaces()[0].getName());
        }
        // 发布事件
        System.out.println("服务注册成功");
    }

    private void publish(final String beanName) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                NettyServer nettyServer = new NettyServer(applicationContext);
                int port = SocketUtils.findAvailableTcpPort();
                registry.register(beanName, getLocalAddressAndPort(port));
                nettyServer.init(port);
            }
        });
        thread.start();
    }

    private String getLocalAddressAndPort(int port) {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
