package com.anthonyzero.utils;

import com.anthonyzero.session.Attributes;
import com.anthonyzero.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>(); //用户id -> Channel
    private static final Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>(); //群组 -》ChannelGroup

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

    /**
     * 通过userid 获取自身channel
     * @param userId
     * @return
     */
    public static Channel getChannel(String userId) {

        return userIdChannelMap.get(userId);
    }


    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            Session session = getSession(channel);
            userIdChannelMap.remove(session.getUserId());
            channel.attr(Attributes.SESSION).set(null);
            System.out.println(session + " 退出登录!");
        }
    }


    public static void bindChannelGroup(String groupId, ChannelGroup channelGroup) {
        groupIdChannelGroupMap.put(groupId, channelGroup);
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return groupIdChannelGroupMap.get(groupId);
    }


    public static void removeChannelGroup(String groupId) {
        groupIdChannelGroupMap.remove(groupId);
    }
}
