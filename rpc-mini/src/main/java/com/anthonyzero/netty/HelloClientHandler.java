package com.anthonyzero.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

//当成功连接后，缓存 ChannelHandlerContext，当调用 call 方法的时候，将请求参数发送到服务端，等待。当服务端收到并返回数据后，调用 channelRead 方法，将返回值赋值个 result，并唤醒等待在 call 方法上的线程。此时，代理对象返回数据
public class HelloClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result; //返回结果
    private String para; //参数
    private Object obj = new Object();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    // 收到服务端响应 唤醒等待线程
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        synchronized (obj) {
            obj.notify();
        }
    }

    public void setPara(String para) {
        this.para = para;
    }

    //发送的时候 放到线程池 处理调用call 方法
    public Object call() throws Exception {
        context.writeAndFlush(para); //发送数据
        synchronized (obj) {
            obj.wait();
        }
        return result; //服务端唤醒之后 result 有数据了 返回结果
    }
}
