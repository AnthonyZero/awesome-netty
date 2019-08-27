package com.anthonyzero.chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

//所有的 Netty 服务器都需要以下两部分。
//1.至少一个 ChannelHandler—该组件实现了服务器对从客户端接收的数据的处理，即它的业务逻辑。
//2.引导—这是配置服务器的启动代码。至少，它会将服务器绑定到它要监听连接请求的端口上。
public class EchoServer {

    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        final EchoServiceHandler handler = new EchoServiceHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //当一个新的连接
                        //被接受时，一个新的子 Channel 将会被创建
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    });

            //对 sync()方法的调用将导致当前 Thread阻塞，一直到绑定操作完成为止
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() +
                    " started and listening for connections on " + future.channel().localAddress());
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException { ;
        new EchoServer(6000).start();
    }
}
