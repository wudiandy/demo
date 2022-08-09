package com.example.demo.api.mq.rabbitmq.encapsulation.producer.broker;

import com.example.demo.api.mq.rabbitmq.encapsulation.api.Message;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.MessageProducer;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.SendCallback;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.exception.MessageRuntimeException;

import java.util.List;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/9
 */
public class ProducerClient implements MessageProducer {
    @Override
    public void send(Message message) throws MessageRuntimeException {

    }

    @Override
    public void send(List<Message> messageList) throws MessageRuntimeException {

    }

    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRuntimeException {

    }
}
