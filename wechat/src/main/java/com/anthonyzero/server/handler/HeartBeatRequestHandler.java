package com.anthonyzero.server.handler;

import com.anthonyzero.protocol.request.HeartBeatRequestPacket;
import com.anthonyzero.protocol.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


//服务端这边只要在收到心跳之后回复客户端，给客户端发送一个心跳响应包即可 也相当于定时向客户端发送心跳包
@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {
    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

    private HeartBeatRequestHandler() {

    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HeartBeatRequestPacket heartBeatRequestPacket) throws Exception {
        channelHandlerContext.writeAndFlush(new HeartBeatResponsePacket());
    }
}
