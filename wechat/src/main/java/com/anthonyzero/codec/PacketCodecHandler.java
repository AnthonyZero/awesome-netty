package com.anthonyzero.codec;

import com.anthonyzero.protocol.Packet;
import com.anthonyzero.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 作为server端的编码解码器  一般先进（解码） 出去（编码）
 */
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();

    private PacketCodecHandler() {

    }

    /**
     * 编码  出方向 发生
     * @param channelHandlerContext
     * @param packet
     * @param list
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, List<Object> list) throws Exception {
        ByteBuf byteBuf = channelHandlerContext.channel().alloc().ioBuffer();
        PacketCodec.INSTANCE.encode(byteBuf, packet);
        list.add(byteBuf);
    }

    /**
     * 解码 入方向 发生
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Packet packet = PacketCodec.INSTANCE.decode(byteBuf);
        list.add(packet);
    }
}
