package com.anthonyzero.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 消费并释放入站消息
 */
@ChannelHandler.Sharable
public class DiscardInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ReferenceCountUtil.release(msg);
    }
    //为 SimpleChannelInboundHandler 的 ChannelInboundHandler 实现。这个实现会在消
    //息被 channelRead0()方法消费之后自动释放消息。
}
