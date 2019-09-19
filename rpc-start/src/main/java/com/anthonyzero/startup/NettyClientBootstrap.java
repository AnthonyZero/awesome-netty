package com.anthonyzero.startup;

import com.anthonyzero.client.RPCProxy;
import com.anthonyzero.common.SysConstant;
import com.anthonyzero.example.User;
import com.anthonyzero.example.UserService;

/**
 * 客户端连接服务端 调用服务
 */
public class NettyClientBootstrap {

    public static void main(String[] args) throws InterruptedException {
        RPCProxy proxy = new RPCProxy(SysConstant.HOST, SysConstant.PORT);
        UserService userService = proxy.createProxy(UserService.class);
        for(;;) {
            Thread.sleep(1000);
            User user = userService.getUser("anthonyzero");
            System.out.println("RPC返回结果：" + user);
        }
    }
}

