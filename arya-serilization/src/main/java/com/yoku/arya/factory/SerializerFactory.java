package com.yoku.arya.factory;

import com.google.common.base.Objects;
import com.yoku.arya.Serializer;

/**
 * 获取对应的序列化方式
 * @author HODO
 */
public class SerializerFactory {

    Serializer getSerialize(Class<? extends Serializer> clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }
}
