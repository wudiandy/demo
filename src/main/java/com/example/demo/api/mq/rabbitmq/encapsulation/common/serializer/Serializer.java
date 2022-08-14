package com.example.demo.api.mq.rabbitmq.encapsulation.common.serializer;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/14
 */
public interface Serializer {

    /**
     * 序列化为byte[]
     *
     * @param data data to be serialized
     * @return byte[] of data
     */
    byte[] serializeRow(Object data);

    /**
     * 序列化为String
     *
     * @param data 待序列化数据
     * @return 序列化后的String
     */
    String serialize(Object data);

    /**
     * 反序列化
     *
     * @param content 待反序列化的内容
     * @param <T>     反序列化的目标类型
     * @return 反序列化后的对象
     */
    <T> T deserialize(String content);

    /**
     * 反序列化
     *
     * @param content 待反序列化的内容
     * @param <T>     反序列化的目标类型
     * @return 反序列化后的对象
     */
    <T> T deserialize(byte[] content);
}
