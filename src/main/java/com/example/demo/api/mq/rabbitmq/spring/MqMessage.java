package com.example.demo.api.mq.rabbitmq.spring;

import lombok.Data;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/3
 */
@Data
public class MqMessage {
    private String queue;
    private String exchange;
    private String routingKey;
    private String content;
}
