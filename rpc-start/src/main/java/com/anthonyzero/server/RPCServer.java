package com.anthonyzero.server;

import com.anthonyzero.codec.ServerCoderHandler;
import com.anthonyzero.common.RPCService;
import com.anthonyzero.common.SysConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class RPCServer implements ApplicationContextAware {

    private Map<String,Object> serviceMap = new HashMap<String,Object>();

    public void startServer(final int port){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioSocketChannel.pipeline();
                            pipeline.addLast(ServerCoderHandler.INSTANCE);
                            pipeline.addLast(new ServerHandler(serviceMap));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind().sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println(">>>>> netty server 启动完成 <<<<<" + "端口[" + port + "]绑定成功!");
                    } else {
                        System.err.println("端口[" + port + "]绑定失败!");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    // spring加载后 调用可获取ApplicationContext
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RPCService.class);
        for (Map.Entry<String,Object> entry: beansWithAnnotation.entrySet()) {
            String interfaceName = entry.getValue().getClass().getAnnotation(RPCService.class).value().getName();
            serviceMap.put(interfaceName, entry.getValue()); //接口名 -》 接口实现类代理类
        }
        System.out.println("代理类实现集合（用户服务端调用）：" + serviceMap);
        //启动netty 服务端
        startServer(SysConstant.PORT);
    }
}
