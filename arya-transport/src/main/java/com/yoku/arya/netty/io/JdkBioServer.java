package com.yoku.arya.netty.io;

import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author xiahj
 * @date 2018/3/5
 */
public class JdkBioServer {

    public static void main(String[] args){
        new JdkBioServer().start();
    }

    public void start() {
        int port = SocketUtils.findAvailableTcpPort();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Port -> " + port);
            for (;;) {
                //阻塞等待 socket
                final Socket clientSocket = serverSocket.accept();
                System.out.println("Accept connection from " + clientSocket);

                try(OutputStream outputStream = clientSocket.getOutputStream();
                    InputStream inputStream = clientSocket.getInputStream()) {
                    byte[] dataSize = new byte[4];
                    inputStream.read(dataSize);

                    byte[] bytes = new byte[byteArrayToInt(dataSize)];

                    //当socket 没有接收到足够的数据写入 byte， read()方法将被阻塞。
                    inputStream.read(bytes);

                    System.out.println("receive a message " + new String(bytes, Charset.forName("UTF-8")));
                    outputStream.write(intToByteArray("Hi!".getBytes(Charset.forName("UTF-8")).length + bytes.length));
                    outputStream.write("Hi!".getBytes(Charset.forName("UTF-8")));
                    outputStream.write(bytes);

                    outputStream.flush();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int byteArrayToInt(byte[] intDate) {
        return (intDate[0] << 24 & 0xff000000) | (intDate[1] << 16 & 0x00ff0000) | (intDate[2] << 8 & 0x0000ff00) | (intDate[3] & 0x000000ff);
    }

    public static byte[] intToByteArray(int size) {
        return new byte[]{(byte) (size >> 24), (byte) (size >> 16), (byte) (size >> 8), (byte) (size)};
    }
}
