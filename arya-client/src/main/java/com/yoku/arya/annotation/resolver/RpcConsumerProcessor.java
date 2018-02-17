package com.yoku.arya.annotation.resolver;

import com.yoku.arya.annotation.RpcConsumer;
import com.yoku.arya.proxy.AryaRpcProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author HODO
 * @date 2018/1/4
 */
public class RpcConsumerProcessor implements BeanPostProcessor, ApplicationContextAware {



    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(final Object bean, String name) throws BeansException {
        final Class<?> clazz = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
        ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                RpcConsumer rpcConsumer = field.getAnnotation(RpcConsumer.class);
                if (rpcConsumer != null) {
                    Class serviceClass = field.getType();
                    Object object = null;
                    try {
                         object = applicationContext.getBean(serviceClass);
                    } catch (Exception ignore) {
                        // do nothing
                    }
                    if (object == null) {
                        object = AryaRpcProxyFactory.getProxyFactory().createProxyBean(serviceClass);
                    }
                    field.setAccessible(true);
                    field.set(bean, object);
                    field.setAccessible(false);
                }
            }
        });
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object object, String beanName) throws BeansException {
        return object;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
