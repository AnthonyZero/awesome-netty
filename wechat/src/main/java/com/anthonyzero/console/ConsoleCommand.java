package com.anthonyzero.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 控制台执行命令
 */
public interface ConsoleCommand {
    void exec(Scanner scanner, Channel channel);
}
