package com.anthonyzero.serialize;

import com.alibaba.fastjson.JSON;

public class JSONSerializer implements Serializer {

    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    public <T> T deserialize(Class<T> clz, byte[] bytes) {
        return JSON.parseObject(bytes, clz);
    }
}
