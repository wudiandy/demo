package com.example.demo.api.mq.rabbitmq.encapsulation.api;

import com.example.demo.api.mq.rabbitmq.encapsulation.api.exception.MessageRuntimeException;

import java.util.List;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/9
 */
public interface MessageProducer {

    /**
     * send message
     *
     * @param message message
     * @throws MessageRuntimeException exception when sending message
     */
    void send(Message message) throws MessageRuntimeException;

    /**
     * send message in batches
     *
     * @param messageList message list
     * @throws MessageRuntimeException exception when sending message
     */
    void send(List<Message> messageList) throws MessageRuntimeException;

    /**
     * send message with callback
     *
     * @param message      message
     * @param sendCallback callback
     * @throws MessageRuntimeException exception when sending message
     */
    void send(Message message, SendCallback sendCallback) throws MessageRuntimeException;
}
