package com.example.demo.api.mq.rabbitmq.spring;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/3
 */
@Slf4j
@Component
public class SpringReceiver {

    /**
     * 用于从RabbitMQ消费消息
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "${spring.rabbitmq.test.queue.name}",
            durable = "${spring.rabbitmq.test.queue.durable}"),
            exchange = @Exchange(name = "${spring.rabbitmq.test.exchange.name}",
                    durable = "${spring.rabbitmq.test.exchange.durable}",
                    type = "${spring.rabbitmq.test.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.test.exchange.ignoreDeclarationExceptions}"),
            key = "${spring.rabbitmq.test.key}"))
    @RabbitHandler
    public void onMessage(Message<Object> message, Channel channel) throws Exception {
        log.info("消费消息：{}...", message.getPayload());
        Thread.sleep(1000);
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);

        if (deliveryTag != null) {
            // 手动签收，只签收当前deliveryTag对应的message
            log.info("消息消费成功");
            channel.basicAck(deliveryTag, false);
        } else {
            throw new Exception("deliveryTag is null");
        }
    }

}
