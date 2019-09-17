package com.anthonyzero.protocol.request;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;

public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_REQUEST;
    }
}
