package com.anthonyzero.protocol.response;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import lombok.Data;

@Data
public class LogoutResponsePacket extends Packet {
    private boolean success;

    private String errorMsg;
    @Override
    public Byte getCommand() {
        return Command.LOGOUT_RESPONSE;
    }
}
