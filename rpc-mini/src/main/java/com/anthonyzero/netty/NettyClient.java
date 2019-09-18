package com.anthonyzero.netty;

import com.anthonyzero.common.HelloService;
import com.anthonyzero.common.SysConstant;

public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        RpcConsumer rpcConsumer = new RpcConsumer();
        HelloService helloService = (HelloService) rpcConsumer.createProxy(HelloService.class, SysConstant.providerName);

        for (;;) {
            Thread.sleep(1000);
            String result = helloService.hello("你好吗？");
            System.out.println(result);
        }
    }
}
