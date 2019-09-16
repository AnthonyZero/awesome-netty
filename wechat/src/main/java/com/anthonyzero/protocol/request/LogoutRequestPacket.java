package com.anthonyzero.protocol.request;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import lombok.Data;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.LOGOUT_REQUEST;
    }
}
