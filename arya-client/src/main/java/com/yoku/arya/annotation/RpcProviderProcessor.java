package com.yoku.arya.annotation;


import com.yoku.arya.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author HODO
 * @date 2018/1/4
 */
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
public class RpcProviderProcessor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    protected void publishService() {
        Map<String, Object> provideServiceMap = applicationContext.getBeansWithAnnotation(RpcProvider.class);
        for (Map.Entry<String, Object> entry : provideServiceMap.entrySet()){
            publish(entry.getKey(), entry.getValue());
        }
        // 发布事件
        System.out.println("服务注册成功");
    }

    private void publish(final String beanName, final Object serviceBean) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                NettyServer nettyServer = new NettyServer(applicationContext);
                nettyServer.init(beanName, serviceBean, 4444);
            }
        });

        thread.start();
    }
}
