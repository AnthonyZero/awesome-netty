package com.anthonyzero.client;

import com.anthonyzero.common.Request;
import com.anthonyzero.codec.RequestEncoder;
import com.anthonyzero.common.Response;
import com.anthonyzero.codec.ResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class ClientHandler extends SimpleChannelInboundHandler<Response> {

    private Response response;
    private Object obj = new Object();
    private String address;
    private int port;

    public ClientHandler(String address, int port) {
        this.address = address;
        this.port = port;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) throws Exception {
        this.response = response; //返回结果
        synchronized (obj) {
            obj.notify();
        }
    }


    // 发送数据 等待client段收到数据 唤醒（代理类中调用此方法 拿到rpc的结果）
    public Response send(Request request) throws InterruptedException {
        ChannelFuture channelFuture = initClient();
        channelFuture.channel().writeAndFlush(request);
        synchronized (obj) {
            obj.wait();
        }
        return this.response;
    }


    // 初始化客户端
    public ChannelFuture initClient() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ResponseDecoder());
                            pipeline.addLast(new RequestEncoder());
                            pipeline.addLast(ClientHandler.this);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(address, port)).sync();
            return channelFuture;
        } catch (Exception e) {
            e.printStackTrace();;
            group.shutdownGracefully();
        }
        return null;
    }
}
