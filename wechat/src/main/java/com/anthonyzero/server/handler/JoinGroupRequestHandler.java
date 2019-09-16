package com.anthonyzero.server.handler;

import com.anthonyzero.protocol.request.JoinGroupRequestPacket;
import com.anthonyzero.protocol.response.JoinGroupResponsePacket;
import com.anthonyzero.utils.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    private JoinGroupRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, JoinGroupRequestPacket joinGroupRequestPacket) throws Exception {
        String groupId = joinGroupRequestPacket.getGroupId();
        JoinGroupResponsePacket response = new JoinGroupResponsePacket();
        response.setGroupId(groupId);

        //获取channelgroup
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        if (channelGroup == null) {
            response.setSuccess(false);
            response.setErrorMsg("群不存在");
            channelHandlerContext.writeAndFlush(response);
            return;
        }

        if (channelGroup.contains(channelHandlerContext.channel())) {
            response.setSuccess(false);
            response.setErrorMsg("已经在群里，无需再次加入");
            channelHandlerContext.writeAndFlush(response);
            return;
        }

        //加群
        response.setSuccess(true);
        channelGroup.add(channelHandlerContext.channel());
        channelHandlerContext.writeAndFlush(response);
    }
}
