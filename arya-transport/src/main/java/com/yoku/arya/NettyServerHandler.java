package com.yoku.arya;

import com.yoku.arya.factory.ProtostuffSerializer;
import com.yoku.arya.factory.SerializerFactory;
import com.yoku.arya.service.DemoServiceImpl;
import com.yoku.arya.service.DemoSrevice;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author HODO
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf buf = ((ByteBuf) msg);
        System.out.println("服务器接收到消息：" + buf.toString());

        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        SerializerFactory serializerFactory = new SerializerFactory();
        Serializer serializer = serializerFactory.getSerialize(ProtostuffSerializer.class);
        RpcRequest rpcRequest = serializer.deserialize(req, RpcRequest.class);
        Object object = handler(rpcRequest);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setObject(object);
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        ctx.writeAndFlush(Unpooled.buffer().writeBytes(serializer.serialize(rpcResponse)));
    }

    private Object handler(RpcRequest rpcRequest) {
        DemoSrevice demoSrevice = new DemoServiceImpl();
        Method method = rpcRequest.getMethod();
        try {
            Method targetMethod = demoSrevice.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            return targetMethod.invoke(demoSrevice, rpcRequest.getParams());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
