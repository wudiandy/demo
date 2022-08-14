package com.example.demo.api.mq.rabbitmq.encapsulation.common.serializer;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/14
 */
public interface SerializerFactory {
    /**
     * 创建Serializer
     *
     * @return Serializer对象
     */
    Serializer create();
}
