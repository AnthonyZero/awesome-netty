package com.anthonyzero.console;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 管理控制台命令
 */
public class ConsoleCommandManager implements ConsoleCommand {

    private Map<String, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {
        consoleCommandMap = new HashMap<>();
        consoleCommandMap.put("send", new SendToUserConsoleCommand());
        consoleCommandMap.put("cg", new CreateGroupConsoleCommand());
        consoleCommandMap.put("jg", new JoinGroupConsoleCommand());
        consoleCommandMap.put("qg", new QuitGroupConsoleCommand());
        consoleCommandMap.put("lg", new ListGroupMembersConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        //  获取第一个指令
        System.out.print("请输入指令: ");
        String command = scanner.next();
        ConsoleCommand consoleCommand = consoleCommandMap.get(command);

        if (consoleCommand != null) {
            consoleCommand.exec(scanner, channel);
        } else {
            System.err.println("无法识别[" + command + "]指令，请重新输入!");
            System.out.println("-----------send: 发消息给某人---------------");
            System.out.println("-----------cg: 创建群聊-------------------");
            System.out.println("-----------jg: 加入群聊-------------------");
            System.out.println("-----------qg: 退出群聊-------------------");
            System.out.println("-----------lg: 获取群成员列表--------------");
            System.out.println("-----------logout: 退出登录---------------");
        }
        waitForLoginResponse();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
