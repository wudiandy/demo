package com.example.demo.api.mq.rabbitmq.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * Spring Boot整个RabbitMQ的sender
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/3
 */
@Slf4j
@Component
public class SpringSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * Confirm消息的回调接口，用于确认消息是否被broker接收
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * @param correlationData   消息的唯一标识。correlationData：相关性数据
         * @param ack               true：broker成功收到消息，false：broker接收信息失败
         * @param cause             失败的原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            if (ack) {
                log.info("消息({})被broker成功接收", correlationData.getId());
            } else {
                log.error("消息({})没有被broker成功接收，原因：{}", correlationData.getId(), cause);
            }
        }
    };

    final MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
        /**
         * 消息发送后的回调函数，可以在消息发送完后做一些处理
         */
        @Override
        public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
            log.info("发送消息完成后的处理...");
            String messageBody = new String(message.getBody());
            log.info("原始消息：{}", messageBody);
            String processedMessage = "处理后的消息 - " + messageBody;
            return new org.springframework.amqp.core.Message(processedMessage.getBytes());
        }
    };

    public void send(MqMessage mqMessage, Map<String, Object> properties) {
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message<Object> objectMessage = MessageBuilder.createMessage(mqMessage.getContent(), messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        String exchangeName = mqMessage.getExchange();
        String routingKey = mqMessage.getRoutingKey();
        // 指定业务唯一ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, objectMessage, messagePostProcessor, correlationData);
    }
}
