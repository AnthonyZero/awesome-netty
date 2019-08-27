package com.anthonyzero.chapter2;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

//每个 Channel 都拥有一个与之相关联的 ChannelPipeline，其持有一个 ChannelHandler 的
//实例链。在默认的情况下，ChannelHandler 会把对它的方法的调用转发给链中的下一个 ChannelHandler
//除了 ChannelInboundHandlerAdapter 之外，还有很多需要学习的 ChannelHandler 的
//子类型和实现
//针对不同类型的事件来调用 ChannelHandler；  应用程序通过实现或者扩展 ChannelHandler 来挂钩到事件的生命周期，并且提供自
//定义的应用程序逻辑；
@Sharable
public class EchoServiceHandler extends ChannelInboundHandlerAdapter {
    //对于每个传入的消息都要调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务端接受数据:" + byteBuf.toString(CharsetUtil.UTF_8));
        ctx.write(byteBuf);
        //将接收到的消息
        //写给发送者，而
        //不冲刷出站消息
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
