package com.anthonyzero.serialize;

public interface Serializer {

    byte[] serialize(Object object);

    <T> T deserialize(Class<T> clz, byte[] bytes);
}
