package com.anthonyzero.client.handler;

import com.anthonyzero.protocol.response.QuitGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, QuitGroupResponsePacket quitGroupResponsePacket) throws Exception {
        if (quitGroupResponsePacket.isSuccess()) {
            System.out.println("退出群聊[" + quitGroupResponsePacket.getGroupId() + "]成功！");
        } else {
            System.err.println("退出群聊[" + quitGroupResponsePacket.getGroupId() + "]失败! 失败原因为："  + quitGroupResponsePacket.getErrorMsg());
        }
    }
}
