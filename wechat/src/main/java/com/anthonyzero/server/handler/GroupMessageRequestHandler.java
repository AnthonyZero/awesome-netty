package com.anthonyzero.server.handler;

import com.anthonyzero.protocol.request.GroupMessageRequestPacket;
import com.anthonyzero.protocol.response.GroupMessageResponsePacket;
import com.anthonyzero.utils.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

//一个 handler 要被多个 channel 进行共享，必须要加上 @ChannelHandler.Sharable 显示地告诉 Netty，这个 handler 是支持多个 channel 共享的，否则会报错
@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {
    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    private GroupMessageRequestHandler() {

    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupMessageRequestPacket groupMessageRequestPacket) throws Exception {
        String groupId = groupMessageRequestPacket.getToGroupId();
        GroupMessageResponsePacket response = new GroupMessageResponsePacket();
        response.setFromGroupId(groupId);

        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        if (channelGroup == null) {
            response.setSuccess(false);
            response.setErrorMsg("群不存在");
            channelHandlerContext.writeAndFlush(response);
            return;
        }

        response.setSuccess(true);
        response.setMessage(groupMessageRequestPacket.getMessage());
        response.setFromUser(SessionUtil.getSession(channelHandlerContext.channel()));
        channelGroup.writeAndFlush(response);  //拿到群聊对应的 channelGroup，写到每个客户端
    }
}
