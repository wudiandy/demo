package com.example.demo.api.mq.rabbitmq;

import com.example.demo.api.mq.rabbitmq.client.Receiver;
import com.example.demo.api.mq.rabbitmq.client.Sender;
import com.example.demo.api.mq.rabbitmq.spring.MqMessage;
import com.example.demo.api.mq.rabbitmq.spring.SpringSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/7/19
 */
@Slf4j
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqController {

    @Resource
    private Sender sender;

    @Resource
    private Receiver receiver;

    @Resource
    private SpringSender springSender;

    @GetMapping("/send")
    public ResponseEntity<String> send() throws IOException, TimeoutException {
        sender.send();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/start/receiving")
    public ResponseEntity<String> startReceiving() throws IOException, TimeoutException {
        receiver.start();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/spring/send")
    public ResponseEntity<String> springSend() {
        final int senderCount = 3;
        AtomicInteger count = new AtomicInteger(10000);

        ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("rabbitmq-message-sender-%d").daemon(true).build();

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> log.error("阻塞队列已满（大小：{}），任务将被丢弃", executor.getQueue().size());

        // 不要在线程中使用Thread.sleep()，如果是业务需要，使用ScheduledExecutorService
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(senderCount + 1, threadFactory, rejectedExecutionHandler);

        CyclicBarrier cyclicBarrier = new CyclicBarrier(senderCount);

        for (int i = 0; i < senderCount; i++) {
            executorService.scheduleAtFixedRate(() -> {
                try {
                    cyclicBarrier.await();
                    while (count.get() > 0) {
                        MqMessage mqMessage = new MqMessage();
                        mqMessage.setContent("message - " + UUID.randomUUID());
                        mqMessage.setExchange("test-exchange");
                        mqMessage.setQueue("test-queue");
                        mqMessage.setRoutingKey("test.abc");
                        Map<String, Object> properties = new HashMap<>(2);
                        properties.put("properties1", "value1");
                        properties.put("properties2", "value2");
                        springSender.send(mqMessage, properties);
                        count.getAndDecrement();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0, 3, TimeUnit.SECONDS);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
