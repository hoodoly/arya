package com.yoku.arya.zk.manager;

/**
 * 不同的注册中心方便扩展实现接口即可
 *
 * @author HODO
 * @date 2018/1/10
 */
public interface RegistryManager {

    String ROOT_PATH = "arya";

    /**
     * 服务注册
     *
     * @param serverName
     * @param serverAddress
     * @return
     */
    boolean registerService(String serverName, String serverAddress);

    /**
     * 注销服务
     *
     * @param serverName
     * @param serverAddress
     * @return
     */
    boolean unregisterService(String serverName, String serverAddress);


    /**
     * 服务消费
     *
     * @param consumerName
     * @return
     */
    String consumerService(String consumerName);
}
