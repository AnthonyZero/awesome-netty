package com.anthonyzero.codec;

import com.anthonyzero.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器   ByteToMessageDecoder Netty会自动进行内存的释放 bytebuf
 * bytebuff -> java对象
 */
public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //通过往这个 List 里面添加解码后的结果对象，就可以自动实现结果往下一个 handler 进行传递
        list.add(PacketCodec.INSTANCE.decode(byteBuf));
    }
}
