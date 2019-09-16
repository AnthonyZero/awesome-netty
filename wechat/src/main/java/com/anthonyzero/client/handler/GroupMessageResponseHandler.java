package com.anthonyzero.client.handler;

import com.anthonyzero.protocol.response.GroupMessageResponsePacket;
import com.anthonyzero.session.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupMessageResponsePacket groupMessageResponsePacket) throws Exception {
        if (groupMessageResponsePacket.isSuccess()) {
            String fromGroupId = groupMessageResponsePacket.getFromGroupId();
            Session fromUser = groupMessageResponsePacket.getFromUser();
            System.out.println("收到群[" + fromGroupId + "]中[" + fromUser.getUserName() + "]发来的消息：" + groupMessageResponsePacket.getMessage());
        } else {
            System.err.println("发群消息[" + groupMessageResponsePacket.getFromGroupId() + "]失败，原因为：" + groupMessageResponsePacket.getErrorMsg());
        }
    }
}
