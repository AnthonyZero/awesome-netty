package com.anthonyzero.client;

import com.anthonyzero.common.Request;
import com.anthonyzero.common.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RPCProxy {
    private String address;
    private int port;

    public RPCProxy(String address, int port) {
        this.address = address;
        this.port = port;
    }

    //JDK 代理
    public <T>T createProxy(Class<?> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Request request = new Request();
                request.setClassName(method.getDeclaringClass().getName());
                request.setMethodName(method.getName());
                request.setParameters(args);
                request.setRequestId(UUID.randomUUID().toString());
                request.setParameterTypes(method.getParameterTypes());
                //启动客户端 发送数据
                ClientHandler client = new ClientHandler(address, port);
                Response response = client.send(request);
                if (response.getError() != null){
                    throw response.getError();
                }
                else{
                    return response.getResult();
                }
            }
        });
    }
}
