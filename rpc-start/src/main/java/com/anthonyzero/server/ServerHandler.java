package com.anthonyzero.server;

import com.anthonyzero.common.Request;
import com.anthonyzero.common.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 服务端服务处理逻辑
 */
public class ServerHandler extends SimpleChannelInboundHandler<Request> {

    private Map<String,Object> serviceMap; //服务注册存放
    public ServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request request) throws Exception {
        Response response = new Response();
        //调用请求类的请求方法执行并返回执行结果
        Object invoke = null;
        try {
            Object requestBean = serviceMap.get(request.getClassName());
            Class<?> requestClass = Class.forName(request.getClassName());
            Method method = requestClass.getMethod(request.getMethodName(), request.getParameterTypes()); //获取执行方法
            invoke = method.invoke(requestBean, request.getParameters()); //调用
            response.setRequestId(request.getRequestId());
            response.setResult(invoke); //结果
        } catch (Exception e) {
            response.setError(e.getCause());
            response.setRequestId(request.getRequestId());
        }
        System.out.println(request + "->" + response);
        channelHandlerContext.writeAndFlush(response);
    }
}
