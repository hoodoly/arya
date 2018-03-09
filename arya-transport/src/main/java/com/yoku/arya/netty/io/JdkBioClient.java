package com.yoku.arya.netty.io;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author xiahj
 * @date 2018/3/7
 */
public class JdkBioClient {

    public static void main(String[] args){
        Socket socket = new Socket();
        OutputStream outputStream = null;
        try {
            socket.connect(new InetSocketAddress("127.0.0.1", 23119));
            outputStream = socket.getOutputStream();
            byte[] sendData = "夏哈哈aaa".getBytes(Charset.forName("UTF-8"));

            outputStream.write(JdkBioServer.intToByteArray(sendData.length));
            outputStream.write(sendData);
            outputStream.flush();

            InputStream inputStream = socket.getInputStream();

            byte[] returnSize = new byte[4];
            inputStream.read(returnSize);
            byte[] returnData = new byte[JdkBioServer.byteArrayToInt(returnSize)];
            inputStream.read(returnData);
            System.out.println("client receive " + new String(returnData));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
