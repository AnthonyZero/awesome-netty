package com.anthonyzero.console;

import com.anthonyzero.protocol.request.LoginRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 登录
 */
public class LoginConsoleCommand implements ConsoleCommand{
    @Override
    public void exec(Scanner scanner, Channel channel) {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        System.out.print("输入用户名登录: ");
        String username = "";
        while (true) {
            username = scanner.next();
            if (username != null && !username.equals("")) {
                break;
            }
        }
        loginRequestPacket.setUserName(username);
        loginRequestPacket.setPassword("password");

        // 发送登录数据包
        channel.writeAndFlush(loginRequestPacket);
        waitForLoginResponse();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
