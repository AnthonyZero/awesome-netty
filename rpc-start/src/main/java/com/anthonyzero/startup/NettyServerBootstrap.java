package com.anthonyzero.startup;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务端启动
 */
public class NettyServerBootstrap {

    public static void main(String[] args) {
        System.out.println(">>>>> netty server 正在启动 <<<<<");
        new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }
}
