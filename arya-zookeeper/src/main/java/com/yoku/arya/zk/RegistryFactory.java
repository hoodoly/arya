package com.yoku.arya.zk;

import com.yoku.arya.zk.manager.RegistryManager;
import org.springframework.util.SocketUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author HODO
 * @date 2018/1/10
 */
public class RegistryFactory implements Registry {

    private static RegistryManager registryManager;


    private RegistryFactory() {

    }

    @Override
    public void register(String serverName, String serverAddress) {
        registryManager.registerService(serverName, getLocalAddressAndPort());
    }

    @Override
    public String consumer(String serverName) {
        return registryManager.consumerService(serverName);
    }

    public static Registry getRegistry(Class<? extends RegistryManager> registryManagerClass) {
        try {
            registryManager = registryManagerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new RegistryFactory();
    }

    private String getLocalAddressAndPort() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + SocketUtils.findAvailableTcpPort();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
