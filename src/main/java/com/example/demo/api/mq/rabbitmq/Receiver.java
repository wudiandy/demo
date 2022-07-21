package com.example.demo.api.mq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/7/19
 */
@Service
@Slf4j
public class Receiver {
    public void start() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.16.255.140");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        // 下面两个配置是每隔3秒检查连接是否中断，如果中断则自动重连
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(3000);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "test_exchange";
        String exchangeType = "direct";
        String routingKey = "test_routingKey";
        String queueName = "testQueue";

        // case of exchangeType is topic
        // String exchangeName = "test_topic_exchangeName";
        // String exchangeType = "topic";
        // String routingKey1 = "test_topic_routingKey.*";
        // String routingKey2 = "*.test_topic_routingKey";
        // String routingKey3 = "test_topic_routingKey.#";
        // String routingKey4 = "#.test_topic_routingKey";

        // case of fanout exchange
        // String exchangeName = "test_fanout_exchange";
        // String exchangeType = "fanout";
        // String queueName = "test_fanout_queue";
        // routingKey = "";

        channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 自动ACK
        channel.basicConsume(queueName, true, consumer);

        long count = 10000;
        while (count > 0) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            log.info("received message: {}", msg);
            count--;
        }
    }
}
