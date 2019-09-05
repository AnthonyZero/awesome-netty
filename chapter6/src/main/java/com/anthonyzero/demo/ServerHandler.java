package com.anthonyzero.demo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        System.out.println(ctx.channel().id() + "的设备加入群聊，此时在线人数："  + channels.size());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().id() + "的设备退出群聊，此时在线人数："  + channels.size());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //打印消息然后群发
        System.out.println("进站收到消息：" + msg.toString());
        for (Channel channel: channels){
            if (channel.id() != ctx.channel().id()) {
                channel.writeAndFlush("发给另一个人:" + msg.toString());
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.channel().id()+"的设备出错，此时在线人数:"+ channels.size());
        ctx.close();
    }
}
