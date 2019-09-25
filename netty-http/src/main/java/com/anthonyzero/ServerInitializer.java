package com.anthonyzero;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

public class ServerInitializer extends ChannelInitializer<NioSocketChannel> {
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
        ChannelPipeline pipeline = nioSocketChannel.pipeline();

        /**
         * 等于使用HttpRequestDecoder & HttpResponseEncoder
         */
        pipeline.addLast(new HttpServerCodec());
        /**
         * 在处理POST消息体时需要加上
         */
        pipeline.addLast(new HttpObjectAggregator(1024*1024));
        pipeline.addLast(new HttpServerExpectContinueHandler());
        pipeline.addLast(new ServerHandler());
    }
}
