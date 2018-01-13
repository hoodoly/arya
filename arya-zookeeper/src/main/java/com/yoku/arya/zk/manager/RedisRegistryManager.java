package com.yoku.arya.zk.manager;

/**
 * @author HODO
 * @date 2018/1/10
 */
public class RedisRegistryManager implements RegistryManager {

    @Override
    public boolean registerService(String serverName, String serverAddress) {
        return false;
    }

    @Override
    public boolean unregisterService(String serverName, String serverAddress) {
        return false;
    }

    @Override
    public String consumerService(String consumerName) {
        return null;
    }
}
