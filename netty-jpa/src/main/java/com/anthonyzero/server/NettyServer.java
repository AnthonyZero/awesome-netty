package com.anthonyzero.server;

import com.anthonyzero.config.NettyConfig;
import com.anthonyzero.server.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Date;

@Component
@Slf4j
public class NettyServer {

    @Autowired
    private NettyConfig config;
    @Autowired
    private ServerChannelInitializer channelInitializer;

    private ChannelFuture channelFuture;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() {
        bossGroup = new NioEventLoopGroup(config.getBossCount());
        workerGroup = new NioEventLoopGroup(config.getWorkerCount());
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(config.getTcpPort()))
                    .option(ChannelOption.SO_BACKLOG, config.getBacklog())
                    .childOption(ChannelOption.SO_KEEPALIVE, config.isKeepAlive())
                    .childHandler(channelInitializer);

            channelFuture = bootstrap.bind().sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        log.info(new Date() + ": 端口[" + config.getTcpPort() + "]绑定成功!");
                    } else {
                        log.info("端口[" + config.getTcpPort() + "]绑定失败!");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @PreDestroy
    public void stop() throws InterruptedException {
        log.info("Stopping Netty Server......");
        try {
            channelFuture.channel().close();
        } finally {
            // 优雅关闭两个 EventLoopGroup 对象
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        log.info("server stopped!");
    }
}
