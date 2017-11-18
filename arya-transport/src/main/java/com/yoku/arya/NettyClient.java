package com.yoku.arya;

import com.yoku.arya.factory.ProtostuffSerializer;
import com.yoku.arya.factory.SerializerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;


/**
 * @author HODO
 */
public class NettyClient {

    private int port;
    private String host;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    SerializerFactory serializerFactory = new SerializerFactory();
    Serializer serializer = serializerFactory.getSerialize(ProtostuffSerializer.class);


    public NettyClient(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public RpcResponse invoker(RpcRequest rpcRequest) throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            final NettyClientHandler clientHandler = new NettyClientHandler();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(clientHandler);
                }});
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();

            serializer.serialize(rpcRequest);
            future.channel().writeAndFlush(Unpooled.buffer().writeBytes(serializer.serialize(rpcRequest)));
            countDownLatch.await();
            // 等待链接关闭
            //future.channel().closeFuture().sync();
            return clientHandler.getRpcResponse();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public class NettyClientHandler extends ChannelInboundHandlerAdapter {


        private RpcResponse rpcResponse;

        /**
         * 接收 Rpc 调用结果
         *
         * @param ctx netty 容器
         * @param msg 服务端答复消息
         * @throws Exception
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            rpcResponse = serializer.deserialize(req, RpcResponse.class);
            countDownLatch.countDown();
        }

        public RpcResponse getRpcResponse() {
            return rpcResponse;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 4444);
        RpcRequest rpcRequest = new RpcRequest("123", null, null, null);
        RpcResponse rpcResponse = nettyClient.invoker(rpcRequest);
        System.out.print(rpcResponse);
    }
}
