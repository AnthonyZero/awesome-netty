package com.anthonyzero.server;

import com.anthonyzero.codec.PacketCodecHandler;
import com.anthonyzero.codec.Spliter;
import com.anthonyzero.server.handler.AuthHandler;
import com.anthonyzero.server.handler.CreateGroupRequestHandler;
import com.anthonyzero.server.handler.LoginRequestHandler;
import com.anthonyzero.server.handler.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.util.Date;

public class WechatServer {

    private static final int PORT = 7000;

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .option(ChannelOption.SO_BACKLOG, 1024) //临时存放已完成三次握手的请求的队列的最大长度
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //开启TCP底层心跳机制
                    .childOption(ChannelOption.TCP_NODELAY, true) //开启Nagle算法，true表示关闭，false表示开启
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline =  nioSocketChannel.pipeline(); // handler顺序 从上到下
                            //拆包器 decode
                            pipeline.addLast(new Spliter());
                            //编码解码器
                            pipeline.addLast(PacketCodecHandler.INSTANCE);
                            pipeline.addLast(LoginRequestHandler.INSTANCE);
                            pipeline.addLast(AuthHandler.INSTANCE);
                            pipeline.addLast(MessageRequestHandler.INSTANCE);
                            pipeline.addLast(CreateGroupRequestHandler.INSTANCE);
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println(new Date() + ": 端口[" + PORT + "]绑定成功!");
                    } else {
                        System.err.println("端口[" + PORT + "]绑定失败!");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync(); //执行,主线程变为wait状态
        }catch (Exception e) {
            e.printStackTrace();
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
