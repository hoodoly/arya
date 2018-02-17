package com.yoku.arya;

import com.yoku.arya.factory.ProtostuffSerializer;
import com.yoku.arya.factory.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

/**
 * @author HODO
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private ApplicationContext applicationContext;

    NettyServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


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
        Method method = rpcRequest.getMethod();
        try {
            if (applicationContext == null) {
                throw new IllegalStateException("application must be init before handler a rpc request");
            }
            Object object = applicationContext.getBean(rpcRequest.getAClass());
            Method targetMethod = object.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            return targetMethod.invoke(object, rpcRequest.getParams());
        } catch (Exception e) {
            log.error("handler rpc request failed", e);
            e.printStackTrace();
        }
        return null;
    }

}
