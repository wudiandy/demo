package com.example.demo.api.mq.rabbitmq.encapsulation.common.serializer.impl;

import com.example.demo.api.mq.rabbitmq.encapsulation.api.Message;
import com.example.demo.api.mq.rabbitmq.encapsulation.common.serializer.Serializer;
import com.example.demo.api.mq.rabbitmq.encapsulation.common.serializer.SerializerFactory;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/14
 */
public class JacksonSerializerFactory implements SerializerFactory {

    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
