package com.anthonyzero.protocol;

import lombok.Data;

/**
 * 通信过程中 Java 对象的抽象类， 数据包
 * 们定义了一个版本号（默认值为 1 ）以及一个获取指令的抽象方法，所有的指令数据包都必须实现这个方法
 */
@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 获取指令
     * @return
     */
    public abstract Byte getCommand();
}
