package com.anthonyzero.server.handler;

import com.anthonyzero.protocol.request.QuitGroupRequestPacket;
import com.anthonyzero.protocol.response.QuitGroupResponsePacket;
import com.anthonyzero.utils.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {
    public static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();

    private QuitGroupRequestHandler() {

    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, QuitGroupRequestPacket quitGroupRequestPacket) throws Exception {
        //获取群对应的 channelGroup，然后将当前用户的 channel 移除
        String groupId = quitGroupRequestPacket.getGroupId();
        QuitGroupResponsePacket response = new QuitGroupResponsePacket();
        response.setGroupId(groupId);

        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        if (channelGroup == null) {
            response.setSuccess(false);
            response.setErrorMsg("群不存在");
            channelHandlerContext.writeAndFlush(response);
            return;
        }

        if (!channelGroup.contains(channelHandlerContext.channel())) {
            response.setSuccess(false);
            response.setErrorMsg("您不在群聊里，无需退出");
            channelHandlerContext.writeAndFlush(response);
            return;
        }

        response.setSuccess(true);
        channelGroup.remove(channelHandlerContext.channel());
        channelHandlerContext.writeAndFlush(response);
    }
}
