package com.anthonyzero.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.anthonyzero.serialize.Serializer;
import com.anthonyzero.serialize.SerializerAlgorithm;

/**
 * 采用fastjson 作为序列化
 */
public class JSONSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
