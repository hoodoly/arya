package com.yoku.arya;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CountDownLatch;


/**
 * @author HODO
 */
public class NettyClient {
    private int port;//服务器端口号
    private String host;//服务器IP

    public NettyClient(String host, int port) throws InterruptedException {
        this.port = port;
        this.host = host;
    }

    public Object invoker(RpcRequest rpcRequest) throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final RpcResponse rpcResponse = new RpcResponse();
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host, port);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new NettyClientHandler(rpcResponse));
                }});
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(rpcRequest).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
            // 等待链接关闭
            future.channel().closeFuture().sync();
            return rpcResponse;
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        NettyClient client = new NettyClient("localhost", 4444);

    }
}
