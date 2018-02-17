package com.yoku.arya;

import java.io.Serializable;

/**
 * @author HODO
 */
public interface Serializer {

    /**
     * 序列化
     * @param object 序列化对象
     * @return 二进制数组
     */
    byte[] serialize(Object object);


    /**
     * 反序列化
     * @param bytes 二进制数组
     * @param tClass 序列化目标类
     * @return 序列化对象
     */
    <T> T deserialize(byte[] bytes, Class<T> tClass);
}
