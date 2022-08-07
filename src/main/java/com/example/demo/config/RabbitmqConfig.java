package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/6
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "custom.rabbitmq")
public class RabbitmqConfig {

    @Data
    static class Queue {
        /**
         * 队列名称
         */
        String name;

        /**
         * 是否持久化
         */
        Boolean durable;
    }

    /**
     * 队列
     */
    Queue queue;

    @Data
    static class Exchange {
        /**
         * 交换器名称
         */
        String name;

        /**
         * 是否持久化
         */
        Boolean durable;

        /**
         * 交换器类型
         */
        String type;

        /**
         * 是否忽略声明异常
         */
        Boolean ignoreDeclarationExceptions;
    }

    /**
     * 交换器
     */
    Exchange exchange;

    /**
     * 路由
     */
    String key;
}
