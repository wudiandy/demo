package com.example.demo.api.mq.rabbitmq.encapsulation.producer.broker;

import com.example.demo.api.mq.rabbitmq.encapsulation.api.Message;

/**
 * 具体发送不同类型消息的接口
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/10
 */
public interface RabbitBroker {

    /**
     * 发送迅速消息
     *
     * @param message 消息
     */
    void rapidSend(Message message);

    /**
     * 发送confirm消息
     *
     * @param message 消息
     */
    void confirmSend(Message message);

    /**
     * 发送可靠消息
     *
     * @param message 消息
     */
    void reliantSend(Message message);

    /**
     * 发送批量消息
     */
    void sendMessages();
}
