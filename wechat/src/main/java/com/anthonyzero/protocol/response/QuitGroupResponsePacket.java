package com.anthonyzero.protocol.response;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import lombok.Data;

@Data
public class QuitGroupResponsePacket extends Packet {
    private String groupId;

    private boolean success;

    private String errorMsg;
    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_RESPONSE;
    }
}
