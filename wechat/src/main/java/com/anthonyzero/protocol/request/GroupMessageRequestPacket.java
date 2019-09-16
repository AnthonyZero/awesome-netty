package com.anthonyzero.protocol.request;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupMessageRequestPacket extends Packet {
    private String toGroupId;
    private String message;

    public GroupMessageRequestPacket(String toGroupId, String message) {
        this.toGroupId = toGroupId;
        this.message = message;
    }
    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_REQUEST;
    }
}
