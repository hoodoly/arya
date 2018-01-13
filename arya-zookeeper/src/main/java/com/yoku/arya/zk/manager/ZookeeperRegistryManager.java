package com.yoku.arya.zk.manager;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author HODO
 * @date 2018/1/10
 */
@Slf4j
public class ZookeeperRegistryManager implements RegistryManager {

    private String zkAddress = "127.0.0.1:2181";

    private static final String SLASH = "/";

    private ZkClient zkClient;

    public ZookeeperRegistryManager(String zkAddress) {
        this.zkAddress = zkAddress;
        initZkClient();
    }

    public ZookeeperRegistryManager() {
        initZkClient();
    }

    private void initZkClient() {
        this.zkClient = new ZkClient(zkAddress);
    }

    @Override
    public boolean registerService(String serverName, String serverAddress) {
        String path = createRegisterPath(serverName, serverAddress);
        return create(path);
    }

    @Override
    public boolean unregisterService(String serverName, String serverAddress) {
        return delete(createRegisterPath(serverName, serverAddress));
    }

    @Override
    public String consumerService(String consumerName) {
        List<String> children = getChildren(createComsumerPath(consumerName));
        if (CollectionUtils.isEmpty(children)) {
            log.error("no server find server name : {}", consumerName);
        } else {
            // TODO: 2018/1/13 负载均衡如何实现
            return children.get(0);
        }
        return null;
    }


    private String createRegisterPath(String serverName, String serverAddress) {
        return SLASH + ROOT_PATH + SLASH + serverName + SLASH + serverAddress;
    }

    private String createComsumerPath(String serverName) {
        return SLASH + ROOT_PATH + SLASH + serverName;
    }

    private boolean create(String path) {
        if (Strings.isNullOrEmpty(path)) {
            return false;
        }
        String[] paths = path.split("/");
        StringBuilder currentPath = new StringBuilder();
        for (String string : paths) {
            if (Strings.isNullOrEmpty(string)) {
                continue;
            }
            currentPath.append("/").append(string);
            if (!exists(currentPath.toString())) {
                zkClient.createPersistent(currentPath.toString());
            }
        }
        return true;
    }

    private List<String> getChildren(String path) {
        return zkClient.getChildren(path);
    }

    private boolean delete(String path) {
        return zkClient.delete(path);
    }


    private boolean exists(String path) {
        return zkClient.exists(path);
    }

    @Override
    protected void finalize() throws Throwable {
        if (zkClient != null) {
            zkClient.close();
        }
    }
}
