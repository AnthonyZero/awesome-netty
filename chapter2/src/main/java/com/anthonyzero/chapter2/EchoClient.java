package com.anthonyzero.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5, 20, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(80));
        for (int i = 0; i < 100; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        /*System.out.println("活跃线程数：" + executor.getActiveCount());*/
                        new EchoClient("127.0.0.1", 6000).start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
    }
}
