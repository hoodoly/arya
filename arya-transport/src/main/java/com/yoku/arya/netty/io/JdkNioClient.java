package com.yoku.arya.netty.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xiahj
 * @date 2018/3/7
 */
public class JdkNioClient {

    public static void main(String[] args) throws InterruptedException {

        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            boolean connected = socketChannel.connect(new InetSocketAddress(56672));
            if (!connected) {
                while (!socketChannel.finishConnect()) {
                    System.out.println("connect...");
                    Thread.sleep(3000);
                }
            }
            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_WRITE);
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isWritable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    client.write(ByteBuffer.wrap("hahah".getBytes()));
                }
                if (selectionKey.isReadable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                    client.read(byteBuffer);
                    System.out.println("client receive message " + new String(byteBuffer.array()));
                    client.close();
                }
                iterator.remove();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
