package com.anthonyzero.chapter6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 保持ChannelHandlerContext的引用
 */
public class WriteHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    public void send(String msg) {
        this.ctx.writeAndFlush(msg);
    }
}
