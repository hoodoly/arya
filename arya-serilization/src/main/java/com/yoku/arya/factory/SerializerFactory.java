package com.yoku.arya.factory;

import com.google.common.base.Objects;
import com.yoku.arya.Serializer;

/**
 * 获取对应的序列化方式
 * @author HODO
 */
public class SerializerFactory {

    public Serializer getSerialize(Class<? extends Serializer> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
