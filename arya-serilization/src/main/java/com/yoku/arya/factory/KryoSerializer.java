package com.yoku.arya.factory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.google.common.base.Throwables;
import com.yoku.arya.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo 序列化方式
 *
 * @author HODO
 */
@Slf4j
public class KryoSerializer implements Serializer {

    @Override
    public byte[] serialize(Object object) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(object.getClass(), new JavaSerializer());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, object);
        output.flush();
        output.close();

        byte[] bytes = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException ex) {
            log.error("kryo serialize object failed cause:{}", Throwables.getStackTraceAsString(ex));
        }
        return bytes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> tClass) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(tClass, new JavaSerializer());
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Input input = new Input(bais);
        return (T) kryo.readClassAndObject(input);
    }

    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            KryoSerializer kryo = new KryoSerializer();
            byte[] bytes = kryo.serialize("qweqwe");
            kryo.deserialize(bytes, String.class);
        }
        System.out.print(System.currentTimeMillis() - t);
    }
}