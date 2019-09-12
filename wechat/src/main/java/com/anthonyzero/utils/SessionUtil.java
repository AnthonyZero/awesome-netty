package com.anthonyzero.utils;

import com.anthonyzero.session.Attributes;
import com.anthonyzero.session.Session;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Channel channel, Session session) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static Session getSession(Channel channel) {

        return channel.attr(Attributes.SESSION).get();
    }

    /**
     * 判断是否登录
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {

        return getSession(channel) != null;
    }
}