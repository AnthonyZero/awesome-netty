package com.anthonyzero.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/*JDK 预置了 interface java.util.concurrent.Future，但是其所提供的实现，
       只允许手动检查对应的操作是否已经完成，或者一直阻塞直到它完成。这是非常繁琐的，所以 Netty
       提供了它自己的实现——ChannelFuture，用于在执行异步操作的时候使用*/

public class ConnectExample {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    public static void connect()  {
        Channel channel = CHANNEL_FROM_SOMEWHERE;

        ChannelFuture channelFuture = channel.connect(new InetSocketAddress("192.168.0.1", 25));
        channelFuture.addListener(new ChannelFutureListener() {
            //注册一个 ChannelFutureListener，
            //以便在操作完成时获得通知
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    //创建一个 ByteBuf 以持有数据
                    ByteBuf byteBuf = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                    ChannelFuture future = channelFuture.channel().writeAndFlush(byteBuf);
                    //将数据异步地发送到远程节点。
                    //返回一个 ChannelFuture
                } else {
                    Throwable throwable = channelFuture.cause();
                    throwable.printStackTrace();
                }
            }
        });
    }
}
