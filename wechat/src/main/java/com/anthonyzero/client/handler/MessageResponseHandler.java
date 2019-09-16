package com.anthonyzero.client.handler;

import com.anthonyzero.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponsePacket messageResponsePacket) throws Exception {
        if (messageResponsePacket.isSuccess()) {
            String fromUserId = messageResponsePacket.getFromUserId();
            String fromUserName = messageResponsePacket.getFromUserName();
            System.out.println("[" + fromUserId + ":" + fromUserName + "]发来消息 -> " + messageResponsePacket
                    .getMessage());
        } else {
            System.err.println(messageResponsePacket.getMessage());
        }
    }
}
