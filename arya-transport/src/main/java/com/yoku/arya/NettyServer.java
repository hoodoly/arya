package com.yoku.arya;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.ApplicationContext;

/**
 * @author HODO
 */
public class NettyServer {

    private ApplicationContext applicationContext;

    public NettyServer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void init(String beanName, Object serviceBean, int port) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.localAddress(port);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline channelPipeline = socketChannel.pipeline();
                    channelPipeline.addLast(new NettyServerHandler(applicationContext));
                }
            });
            ChannelFuture f = bootstrap.bind().sync();
            if (f.isSuccess()) {
                System.out.println("启动Netty服务成功，端口号：" + port);
            }

            registerZookeeper(beanName, serviceBean, "127.0.0.1", port);
            //Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("启动Netty服务异常，异常信息：" + e.getMessage());
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private void registerZookeeper(String name, Object object, String ipAddress, int port) {

    }

}
