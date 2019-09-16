package com.anthonyzero.console;

import com.anthonyzero.protocol.request.CreateGroupRequestPacket;
import com.anthonyzero.utils.SessionUtil;
import io.netty.channel.Channel;

import java.util.*;

public class CreateGroupConsoleCommand implements ConsoleCommand{
    private static final String USER_ID_SPLITER = ",";
    @Override
    public void exec(Scanner scanner, Channel channel) {
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();

        System.out.print("【拉人群聊】输入 userId 列表，userId 之间英文逗号隔开：");
        String currentUserId = SessionUtil.getSession(channel).getUserId();
        String userIds = scanner.next();
        Set<String> set = new HashSet<>(Arrays.asList(userIds.split(USER_ID_SPLITER)));
        set.add(currentUserId); //加上发起者
        createGroupRequestPacket.setUserIdList(new ArrayList<>(set));

        channel.writeAndFlush(createGroupRequestPacket);
    }
}
