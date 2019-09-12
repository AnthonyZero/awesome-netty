package com.anthonyzero.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .remoteAddress(new InetSocketAddress("127.0.0.1", 7000))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder",new StringDecoder()); //需添加编码解码器 否则操作数据相关事件如read不回调
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new ClientHandler());
                            pipeline.addLast(new ClientOutHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect().sync();
            Scanner sca = new Scanner(System.in);
            while (true) {
                String demand = sca.next();
                if ("exit".equals(demand)) {
                    break;
                }
                future.channel().writeAndFlush(demand);
            }
            /*future.channel().closeFuture().sync(); 这会阻塞*/
        } finally {
            group.shutdownGracefully();  //优雅关闭
        }
    }
}
