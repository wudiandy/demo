package com.example.demo.api.mq.rabbitmq.encapsulation.producer.broker;

import com.example.demo.api.mq.rabbitmq.encapsulation.api.Message;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.MessageProducer;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.MessageType;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.SendCallback;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.exception.MessageRuntimeException;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/9
 */
@Component
public class ProducerClient implements MessageProducer {

    @Resource
    RabbitBroker rabbitBroker;

    @Override
    public void send(Message message) throws MessageRuntimeException {
        Preconditions.checkNotNull(message.getTopic());
        String messageType = message.getMessageType();
        switch (messageType) {
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void send(List<Message> messageList) throws MessageRuntimeException {

    }

    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRuntimeException {

    }
}
