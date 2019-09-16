package com.anthonyzero.server.handler;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<Packet> {
    public static final IMHandler INSTANCE = new IMHandler();

    private Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap; //存放指令到各个指令处理器的映射

    private IMHandler() {
        handlerMap = new HashMap<>();

        handlerMap.put(Command.MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestHandler.INSTANCE);
        handlerMap.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestHandler.INSTANCE);
        handlerMap.put(Command.LOGOUT_REQUEST, LogoutRequestHandler.INSTANCE);

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        handlerMap.get(packet.getCommand()).channelRead(channelHandlerContext, packet);
    }
}
