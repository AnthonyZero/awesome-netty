package com.anthonyzero.protocol;

public interface Command {

    Byte LOGIN_REQUEST = 1; //登录请求

    Byte LOGIN_RESPONSE = 2; //登录服务响应

    Byte MESSAGE_REQUEST = 3; //消息请求

    Byte MESSAGE_RESPONSE = 4; //消息响应

    Byte LOGOUT_REQUEST = 5; //退出请求

    Byte LOGOUT_RESPONSE = 6; //退出响应

    Byte CREATE_GROUP_REQUEST = 7; //创建群聊请求

    Byte CREATE_GROUP_RESPONSE = 8; //创建群聊响应

    Byte LIST_GROUP_MEMBERS_REQUEST = 9; //获取群成员

    Byte LIST_GROUP_MEMBERS_RESPONSE = 10;

    Byte JOIN_GROUP_REQUEST = 11; //假如群聊

    Byte JOIN_GROUP_RESPONSE = 12;

    Byte QUIT_GROUP_REQUEST = 13; //退出群聊

    Byte QUIT_GROUP_RESPONSE = 14;

    Byte GROUP_MESSAGE_REQUEST = 15; //发群聊消息

    Byte GROUP_MESSAGE_RESPONSE = 16;

    Byte HEARTBEAT_REQUEST = 17;

    Byte HEARTBEAT_RESPONSE = 18;
}
