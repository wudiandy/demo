package com.example.demo.api.mq.rabbitmq.encapsulation.producer.broker;

import com.example.demo.api.mq.rabbitmq.encapsulation.api.Message;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.MessageType;
import com.example.demo.api.mq.rabbitmq.encapsulation.producer.constant.BrokerMessageConst;
import com.example.demo.api.mq.rabbitmq.encapsulation.producer.constant.BrokerMessageStatus;
import com.example.demo.api.mq.rabbitmq.encapsulation.producer.entity.BrokerMessage;
import com.example.demo.api.mq.rabbitmq.encapsulation.producer.mapper.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/10
 */
@Slf4j
@Component
public class RabbitBrokerImpl implements RabbitBroker {

    @Resource
    private RabbitTemplateContainer rabbitTemplateContainer;

    @Resource
    private MessageStoreService messageStoreService;

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
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        message.setMessageType(MessageType.RELIANT);

        // 1、把数据库的消息发送日志先记录好
        Date now = new Date();
        BrokerMessage brokerMessage = new BrokerMessage();
        brokerMessage.setMessageId(message.getMessageId());
        brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
        brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
        brokerMessage.setCreateTime(now);
        brokerMessage.setUpdateTime(now);
        brokerMessage.setMessage(message);
        messageStoreService.insert(brokerMessage);

        // 2、执行真正地发送消息逻辑
        sendKernel(message);
    }

    @Override
    public void sendMessages() {

    }
}
