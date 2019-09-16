package com.anthonyzero.protocol.response;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId; //发送方userId

    private String fromUserName; //发送方userName

    private String message;  //发送的消息

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
