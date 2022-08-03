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
                log.info("message({}) is accepted by broker successfully", correlationData.toString());
            } else {
                log.error("message({}) is not accepted by broker, cause: {}", correlationData.toString(), cause);
            }
        }
    };

    final MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
        /**
         * 消息发送后的回调函数，可以在消息发送完后做一些处理
         */
        @Override
        public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
            log.info("process after sending message...");
            log.info("origin message: {}", message.toString());
            String processedMessage = "processed - " + message;
            return new org.springframework.amqp.core.Message(processedMessage.getBytes());
        }
    };

    public void send(Object message, Map<String, Object> properties) throws Exception {
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message<Object> objectMessage = MessageBuilder.createMessage(message, messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        String exchangeName = "";
        String routingKey = "";
        // 指定业务唯一ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, objectMessage, messagePostProcessor, correlationData);
    }
}
