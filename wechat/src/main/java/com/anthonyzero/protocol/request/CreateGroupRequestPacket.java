package com.anthonyzero.protocol.request;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupRequestPacket extends Packet {
    private List<String> userIdList;
    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }
}
