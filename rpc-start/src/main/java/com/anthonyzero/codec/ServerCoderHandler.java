package com.anthonyzero.codec;

import com.anthonyzero.common.Request;
import com.anthonyzero.common.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 服务端解码 编码
 */
@ChannelHandler.Sharable
public class ServerCoderHandler extends MessageToMessageCodec<ByteBuf, Response> {
    public static final ServerCoderHandler INSTANCE = new ServerCoderHandler();

    private ServerCoderHandler() {
    }

    // 出方向
    protected void encode(ChannelHandlerContext channelHandlerContext, Response response, List<Object> list) throws Exception {
        ByteBuf byteBuf = channelHandlerContext.channel().alloc().ioBuffer();
        byteBuf.writeBytes(SerializationUtil.serialize(response));
        list.add(byteBuf);
    }

    //入方向
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        Request request = SerializationUtil.deserialize(bytes, Request.class);
        list.add(request);
    }
}
