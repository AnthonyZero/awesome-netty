package com.anthonyzero.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * 丢弃并释放出站消息
 * 在出站方向这边，如果你处理了 write()操作并丢弃了一个消息，那么你也应该负责释放它。
 */
@ChannelHandler.Sharable
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ReferenceCountUtil.release(msg);
        promise.setSuccess();//通知 ChannelPromise数据已经被处理了
    }


    //每一个新创建的 Channel 都将会被分配一个新的 ChannelPipeline。这项关联是永久性
    //的；Channel 既不能附加另外一个 ChannelPipeline，也不能分离其当前的。在 Netty 组件
    //的生命周期中，这是一项固定的操作，不需要开发人员的任何干预。
}
