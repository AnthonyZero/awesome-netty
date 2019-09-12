package com.anthonyzero.server.handler;

import com.anthonyzero.protocol.request.LoginRequestPacket;
import com.anthonyzero.protocol.response.LoginResponsePacket;
import com.anthonyzero.session.Session;
import com.anthonyzero.utils.IDUtil;
import com.anthonyzero.utils.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 客户端登录请求 处理
 */
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginResponsePacket response = new LoginResponsePacket(); //响应
        response.setVersion(loginRequestPacket.getVersion());
        response.setUserName(loginRequestPacket.getUserName());

        if (valid(loginRequestPacket)) {
            response.setSuccess(true);
            String userId = IDUtil.randomId(); //这随机生成一个userid 作为用户标识
            response.setUserId(userId);
            System.out.println("[" + loginRequestPacket.getUserName() + "]登录成功");
            SessionUtil.bindSession(channelHandlerContext.channel(), new Session(userId, loginRequestPacket.getUserName()));
        } else {
            response.setReason("账号密码校验失败");
            response.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        // 登录响应
        channelHandlerContext.writeAndFlush(response);
    }

    /**
     * 默认都登录成功
     * @param loginRequestPacket
     * @return
     */
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
