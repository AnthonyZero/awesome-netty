package com.anthonyzero.codec;

import com.anthonyzero.common.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 客户端 编码器
 */
public class RequestEncoder extends MessageToByteEncoder<Request> {
    protected void encode(ChannelHandlerContext channelHandlerContext, Request request, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(SerializationUtil.serialize(request));
    }
}
