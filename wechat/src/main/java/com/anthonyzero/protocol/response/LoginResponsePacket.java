package com.anthonyzero.protocol.response;

import com.anthonyzero.protocol.Command;
import com.anthonyzero.protocol.Packet;
import lombok.Data;

@Data
public class LoginResponsePacket extends Packet {
    private String userId;

    private String userName;

    private boolean success; //登录成功与否

    private String reason; //登录失败原因

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
