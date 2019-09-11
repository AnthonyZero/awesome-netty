package com.anthonyzero.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
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