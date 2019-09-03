package com.anthonyzero.chapter6;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultChannelPipeline;

public class DummyChannelPipeline extends DefaultChannelPipeline {
    public static final ChannelPipeline DUMMY_INSTANCE = new DummyChannelPipeline(null);

    protected DummyChannelPipeline(Channel channel) {
        super(channel);
    }
}
