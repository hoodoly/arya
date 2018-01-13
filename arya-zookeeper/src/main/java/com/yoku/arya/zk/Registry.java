package com.yoku.arya.zk;

/**
 * @author HODO
 * @date 2018/1/10
 */
public interface Registry {


    /**
     * 注册中心服务注册核心方法
     *
     * @param serverName    服务名 建议 xxx.xxx.XxxService
     * @param serverAddress 服务地址 127.0.0.1:5673
     */
    void register(String serverName, String serverAddress);

    /**
     * 服务发现
     * 通过服务全限定名获取对应服务的地址和端口号
     *
     * @param serverName 服务名
     * @return 服务地址
     */
    String consumer(String serverName);
}
