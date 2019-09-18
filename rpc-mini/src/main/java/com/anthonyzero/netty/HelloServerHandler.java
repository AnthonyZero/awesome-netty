package com.anthonyzero.netty;

import com.anthonyzero.common.HelloServiceImpl;
import com.anthonyzero.common.SysConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //符合我们约定的服务  就处理调用
        if (msg.toString().startsWith(SysConstant.providerName)) {
            String param  = msg.toString().substring(msg.toString().lastIndexOf("#") + 1); //获取我们的参数
            String result = new HelloServiceImpl().hello(param);
            ctx.writeAndFlush(result);
        }
    }
}
