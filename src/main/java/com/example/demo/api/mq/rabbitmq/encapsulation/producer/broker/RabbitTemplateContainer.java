package com.example.demo.api.mq.rabbitmq.encapsulation.producer.broker;

import com.example.demo.api.mq.rabbitmq.encapsulation.api.Message;
import com.example.demo.api.mq.rabbitmq.encapsulation.api.MessageType;
import com.example.demo.api.mq.rabbitmq.encapsulation.common.convert.GenericConverter;
import com.example.demo.api.mq.rabbitmq.encapsulation.common.convert.RabbitMessageConverter;
import com.example.demo.api.mq.rabbitmq.encapsulation.common.serializer.Serializer;
import com.example.demo.api.mq.rabbitmq.encapsulation.common.serializer.SerializerFactory;
import com.example.demo.api.mq.rabbitmq.encapsulation.common.serializer.impl.JacksonSerializerFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * RabbitTemplate对象池
 * <p>
 * 单例的RabbitTemplate对象在处理速度上比较慢，当遇到不同对消息发送需求的时候，需要不断的RabbitTemplate对象进行属性设置
 * 这个过程在高并发的场景下会拖慢消息发送性能，所以可以对RabbitTemplate对象进行池化处理。针对不同的topic提前创建好对象。
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/14
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

    // TODO 这里为什么要用ConcurrentMap呢？

    private final Map<String, RabbitTemplate> rabbitTemplateMap = Maps.newConcurrentMap();

    /**
     * 注意ConnectionFactory要使用springframework的，因为RabbitTemplate用的也是springframework的
     */
    @Resource
    private ConnectionFactory connectionFactory;

    private final SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    public RabbitTemplate getTemplate(Message message) {
        Preconditions.checkNotNull(message.getTopic());
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitTemplateMap.get(topic);
        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }

        log.info("#RabbitTemplateContainer.getTemplate# top {} is not exists, create one", topic);

        RabbitTemplate newTemplate = new RabbitTemplate(connectionFactory);
        newTemplate.setExchange(topic);
        newTemplate.setRoutingKey(message.getRoutingKey());
        newTemplate.setRetryTemplate(new RetryTemplate());

        // 添加序列化反序列化converter对象
        Serializer serializer = serializerFactory.create();
        GenericConverter genericConverter = new GenericConverter(serializer);
        RabbitMessageConverter rabbitMessageConverter = new RabbitMessageConverter(genericConverter);
        newTemplate.setMessageConverter(rabbitMessageConverter);

        String messageType = message.getMessageType();
        if (!MessageType.RAPID.equals(messageType)) {
            newTemplate.setConfirmCallback(this);
        }
        rabbitTemplateMap.put(topic, newTemplate);
        return rabbitTemplateMap.get(topic);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String messageId = Objects.requireNonNull(correlationData).getId().split("#")[0];
        long sendTime = Long.parseLong(correlationData.getId().split("#")[1]);

        if (ack) {
            log.info("send message is OK, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        } else {
            log.error("send message is fail, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        }
    }
}
