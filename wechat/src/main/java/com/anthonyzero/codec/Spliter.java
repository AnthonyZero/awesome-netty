package com.anthonyzero.codec;

import com.anthonyzero.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 拆包器
 */
public class Spliter extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FIELD_OFFSET = 7; //长度域偏移量 4 + 1 + 1 + 1
    private static final int LENGTH_FIELD_LENGTH = 4; //长度域的长度  4

    public Spliter() {
        //第一个参数指的是数据包的最大长度，第二个参数指的是长度域的偏移量，第三个参数指的是长度域的长度
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    /**
     * 在 decode 之前判断前四个字节是否是等于我们定义的魔数 来拒绝非本协议连接
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.getInt(in.readerIndex()) != PacketCodec.MAGIC_NUMBER) {
            // 屏蔽非本协议的客户端
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
