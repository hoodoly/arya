package com.yoku.arya.netty.io;

import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xiahj
 * @date 2018/3/7
 */
public class JdkNioServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        int port = SocketUtils.findAvailableTcpPort();
        System.out.println("port -> " + port);
        serverChannel.bind(new InetSocketAddress(port));


        Selector selector = Selector.open();

        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer message = ByteBuffer.wrap("Hi! Xia".getBytes());

        for (;;) {
            //阻塞直到有可处理的 IO 事件产生
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel client = server.accept();
                    if (client != null) {
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ, message.duplicate());
                        System.out.println("Accept connection from client " + client);
                    }
                }
//                if (selectionKey.isWritable()) {
//                    SocketChannel client = ((SocketChannel) selectionKey.channel());
//                    client.write(message);
//
//                }
                if (selectionKey.isReadable()) {
                    SocketChannel client = ((SocketChannel) selectionKey.channel());
                    ByteBuffer byteBuffer = ByteBuffer.allocate(200);
                    client.read(byteBuffer);
                    System.out.println("Receive message " + new String(byteBuffer.array()));
                }
            }
        }
    }
}
