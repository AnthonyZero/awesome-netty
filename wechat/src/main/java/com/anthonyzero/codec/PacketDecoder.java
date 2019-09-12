package com.anthonyzero.codec;

import com.anthonyzero.protocol.Packet;
import com.anthonyzero.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * client
 * 解码器
 * bytebuff -> java对象  入方向
 */
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Packet packet = PacketCodec.INSTANCE.decode(byteBuf);
        list.add(packet);
    }
}
