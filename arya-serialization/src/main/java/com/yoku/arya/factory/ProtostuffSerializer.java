package com.yoku.arya.factory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.yoku.arya.Serializer;

/**
 * @author HODO
 */
public class ProtostuffSerializer implements Serializer {

    @Override
    public byte[] serialize(Object object) {
        Class targetClass = object.getClass();
        RuntimeSchema schema = RuntimeSchema.createFrom(targetClass);
        LinkedBuffer linkedBuffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return ProtostuffIOUtil.toByteArray(object, schema, linkedBuffer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> targetClass) {
        RuntimeSchema schema = RuntimeSchema.createFrom(targetClass);
        T object = (T) schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, object, schema);
        return object;
    }
}
