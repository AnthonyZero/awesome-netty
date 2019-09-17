package com.anthonyzero.protocol.response;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;

public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}
