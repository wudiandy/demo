package com.example.demo.api.mq.rabbitmq.encapsulation.producer.broker;

import com.example.demo.api.mq.rabbitmq.encapsulation.api.Message;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/10
 */
@Slf4j
@Component
public class RabbitBrokerImpl implements RabbitBroker {

    @Resource
    private RabbitTemplateContainer rabbitTemplateContainer;

    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    /**
     * 发送消息的核心方法，使用异步线程池发送消息
     *
     * @param message 待发送的消息
     */
    private void sendKernel(Message message) {
        AsyncBaseQueue.submit(() -> {
            CorrelationData correlationData = new CorrelationData(
                    String.format("%s#%s",
                            message.getMessageId(),
                            System.currentTimeMillis()));
            String exchange = message.getTopic();
            String routingKey = message.getRoutingKey();
            rabbitTemplateContainer
                    .getTemplate(message)
                    .convertAndSend(exchange, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq, message id = {}", message.getMessageId());
        });
    }

    @Override
    public void confirmSend(Message message) {

    }

    @Override
    public void reliantSend(Message message) {

    }

    @Override
    public void sendMessages() {

    }
}
