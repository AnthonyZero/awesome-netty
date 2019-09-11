package com.anthonyzero.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class WechatClient {
    private static final int MAX_RETRY = 5; //重试次数
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 7000;


    public static void main(String[] args) {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) //连接的超时时间
                    .option(ChannelOption.SO_KEEPALIVE, true) //开启 TCP 底层心跳机制
                    .option(ChannelOption.TCP_NODELAY, true) //是否开始 Nagle 算法，true 表示关闭，false 表示开启
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                        }
                    });

            connect(bootstrap, 5);
        } catch (Exception e) {
            workGroup.shutdownGracefully();
        }
    }


    private static void connect(Bootstrap bootstrap, int retry) {
        bootstrap.connect(HOST, PORT).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                Scanner sca = new Scanner(System.in);
                while (true) {
                    String demand = sca.next();
                    if ("exit".equals(demand)) {
                        break;
                    }
                    channel.writeAndFlush(demand);
                }
                channel.closeFuture().sync();
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                //重试
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的任务执行间隔
                int delay = order << 1;  //2的幂
                System.err.println(new Date() + ": 连接失败，进行第" + order + "次重连……");
                // 隔xx秒 执行重连
                bootstrap.config().group().schedule(() -> connect(bootstrap, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }
}
