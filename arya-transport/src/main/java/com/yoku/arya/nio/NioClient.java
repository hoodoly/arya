package com.yoku.arya.nio;

import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @author xiahj
 * @date 2018/2/17
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        int availableTcpPort = SocketUtils.findAvailableTcpPort(5000);
        System.out.println(availableTcpPort);
        serverSocketChannel.bind(new InetSocketAddress(availableTcpPort));

        serverSocketChannel.accept();
    }
}
