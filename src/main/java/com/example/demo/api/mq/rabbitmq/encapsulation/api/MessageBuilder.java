package com.example.demo.api.mq.rabbitmq.encapsulation.api;

import com.example.demo.api.mq.rabbitmq.encapsulation.api.exception.MessageRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * message建造者
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/9
 */
public class MessageBuilder {

    private String messageId;
    private String topic;
    private String routingKey = "";
    private Map<String, Object> attributes = new HashMap<>();
    private int delayMills;
    private String messageType = MessageType.CONFIRM;

    /**
     * 注意：建造者模式，构造函数要私有化
     */
    private MessageBuilder() {
    }

    private MessageBuilder(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills, String messageType) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMills = delayMills;
        this.messageType = messageType;
    }

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder withRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder withAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public MessageBuilder withAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    public MessageBuilder withDelayMills(int delayMills) {
        this.delayMills = delayMills;
        return this;
    }

    public MessageBuilder withMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public MessageBuilder build() {
        if (this.messageId == null) {
            this.messageId = UUID.randomUUID().toString();
        }

        if (this.topic == null) {
            throw new MessageRuntimeException("topic is null");
        }

        return new MessageBuilder(messageId, topic, routingKey, attributes, delayMills, messageType);
    }
}
