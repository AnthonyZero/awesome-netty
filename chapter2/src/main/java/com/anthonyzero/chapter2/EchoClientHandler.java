package com.anthonyzero.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@Sharable //标记该类的实例可以被多个 Channel 共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    //在到服务器的连接已经建立之后将被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty start!", CharsetUtil.UTF_8));
    }


    //当从服务器接收到一条消息时被调用；
    //需要注意的是，由服务器发送的消息可能会被分块接收
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println(Thread.currentThread().getName() + "->客户端收到：" + byteBuf.toString(CharsetUtil.UTF_8));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
