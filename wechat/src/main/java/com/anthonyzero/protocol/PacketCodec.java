package com.anthonyzero.protocol;

import com.anthonyzero.protocol.request.*;
import com.anthonyzero.protocol.response.*;
import com.anthonyzero.serialize.Serializer;
import com.anthonyzero.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * 魔数 + 版本号（1字节） + 序列化算法（1字节） + 指令（1字节） + 数据长度  + 数据内容
 * 封装成二进制
 */
public class PacketCodec {


    public static final int MAGIC_NUMBER = 0x12345678; //第一个字段是魔数 4个字节
    private final Map<Byte, Class<? extends Packet>> packetTypeMap; //每个指令 对应一个数据包
    private final Map<Byte, Serializer> serializerMap; //序列化算法 对应 它的实现

    public static final PacketCodec INSTANCE = new PacketCodec();  //实例 单例

    private PacketCodec()  {
        // 对于每一种业务handler 所对应的请求 响应数据 要记住添加在这 否则序列化失败
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        packetTypeMap.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        packetTypeMap.put(Command.LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);
        packetTypeMap.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        packetTypeMap.put(Command.GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);
        packetTypeMap.put(Command.LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(Command.LOGOUT_RESPONSE, LogoutResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    /**
     * 数据编码 -》 bytebuf
     * @param byteBuf
     * @param packet
     */
    public void encode(ByteBuf byteBuf, Packet packet) {
        //序列化数据包 java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        //实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    /**
     * 数据解析
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number 4个字节
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        //数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestPacket = packetTypeMap.get(command); //数据包class
        Serializer serializer = serializerMap.get(serializeAlgorithm); //序列算法实现
        if (requestPacket != null && serializer != null) {
            return serializer.deserialize(requestPacket, bytes); //反序列化
        }
        return null;
    }
}
