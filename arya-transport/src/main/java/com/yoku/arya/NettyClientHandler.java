package com.yoku.arya;


import java.io.UnsupportedEncodingException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @author HODO
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private  ByteBuf firstMessage;

    private RpcResponse rpcResponse;

    public NettyClientHandler(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        byte[] data = "client send a message".getBytes();
//        firstMessage=Unpooled.buffer();
//        firstMessage.writeBytes(data);
//        ctx.writeAndFlush(firstMessage);
//    }

    /**
     * 接收 Rpc 调用结果
     * @param ctx netty 容器
     * @param msg 服务端答复消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client receive " + msg);
        rpcResponse.setObject(msg);
        rpcResponse.setRequestId("121213");
    }
}
