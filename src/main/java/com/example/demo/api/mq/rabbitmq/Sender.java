package com.example.demo.api.mq.rabbitmq;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/7/19
 */
@Service
@Slf4j
public class Sender {
    public void send() throws IOException, TimeoutException {
        String msg = "Hello RabbitMQ";

        // 1、创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.16.255.140");
        connectionFactory.setPort(5672);
        // 用于定义业务域
        connectionFactory.setVirtualHost("/");

        // 2、创建Connection
        Connection connection = connectionFactory.newConnection();

        // 3、创建Channel
        Channel channel = connection.createChannel();

        // case of topic exchange type
        // String exchangeName = "test_topic_exchange";
        // String routingKey1 = "test_topic_routingKey.abc";
        // String routingKey2 = "test_topic_routingKey.abc.def";
        // String routingKey3 = "abc.test_topic_routingKey";
        // String routingKey4 = "abc.def.test_topic_routingKey.abc";

        // case of fanout exchange
        // 广播模式，不需要exchange的计算，直接到队列，所以不用设置routingKey
        // 这种方式的效率最高
        // String exchangeName = "test_fanout_exchange";
        // String exchangeType = "fanout";
        // String queueName = "test_fanout_queue";
        // String routingKey = "";

        // there is no need to set queueName in Sender the same as the one in Receiver, so just giving a name you like
        String queueName = "testQueue";
        String exchangeName = "test_exchange";
        String routingKey = "test_routingKey";

        // 4、声明队列
        // 队列名称：test001
        // 持久化：否
        // 独占队列：否
        // 自动删除队列：否
        channel.queueDeclare(queueName, false, false, false, null);

        // 注意：要通过控制台来配置queue，exchange以及它们之间的绑定关系。
        // 上面的代码只表示可以用过Java代码实现MQ的配置。

        // 开启异步确认消息投递成功
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) {
                log.info("push to MQ successfully");
                // MQ成功投递应答后的业务
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) {
                log.error("failed to push message to MQ");
                // MQ投递失败后的业务逻辑
            }
        });

        // 同步消息投递确认，用于处理不可路由的消息，这个机制可以对不可路由的消息处理做一个兜底
        channel.addReturnListener((replyCode, replyText, exchange, routingKey1, properties, body) -> {
            log.info("handleReturn");
            log.info("replyCode = {}", replyCode);
            log.info("replyText = {}", replyText);
            log.info("exchange = {}", exchange);
            log.info("routingKey = {}", routingKey1);
            log.info("body = {}", new String(body));
        });
        boolean mandatory = true;
        channel.basicPublish(exchangeName, routingKey, mandatory, null, msg.getBytes());

        // 通过headers，我们可以自定义一些自己的属性
        Map<String, Object> headers = new HashMap<>(16);

        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                // 2表示持久化消息
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .headers(headers)
                // TTL为6s
                .expiration("6000")
                .build();

        int sendNum = 50;
        for (int i = 0; i < sendNum; i++) {
            log.info("正在发送消息{}...", i);
            msg = "mq message " + i;
            // 如果没有指定exchange，RabbitMQ会走默认的exchange（AMQP default）
            channel.basicPublish(exchangeName, routingKey, props, msg.getBytes());
        }

        // 如果开启了Confirm监听或者return监听，则不能在发完消息后马上关闭
        channel.close();
        connection.close();
    }
}
