package com.anthonyzero.protocol;

public interface Command {

    Byte LOGIN_REQUEST = 1; //登录请求

    Byte LOGIN_RESPONSE = 2; //登录服务响应

    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;
}
