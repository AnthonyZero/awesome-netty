package com.anthonyzero.protocol.response;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import com.anthonyzero.session.Session;
import lombok.Data;

@Data
public class GroupMessageResponsePacket extends Packet {
    private String fromGroupId;

    private Session fromUser;

    private String message;

    private boolean success;

    private String errorMsg;
    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }
}
