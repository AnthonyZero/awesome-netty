package com.anthonyzero.protocol.request;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import lombok.Data;

@Data
public class QuitGroupRequestPacket extends Packet {
    private String groupId;
    @Override
    public Byte getCommand() {
        return Command.QUIT_GROUP_REQUEST;
    }
}
