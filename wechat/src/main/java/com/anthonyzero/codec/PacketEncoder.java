package com.anthonyzero.codec;

import com.anthonyzero.protocol.Packet;
import com.anthonyzero.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器  对我们的java对象 packet 进行编码 -》字节
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        PacketCodec.INSTANCE.encode(byteBuf, packet);  //写入bytebuf中
    }
}
