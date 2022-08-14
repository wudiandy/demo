package com.example.demo.api.mq.rabbitmq.encapsulation.producer.autoconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 这个类型写在了spring.factory中，并且这个类用@Configuration修饰
 * 这样在Spring Boot启动的时候就会自动加载配置
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/9
 */
@Configuration
@ComponentScan({"com.example.demo.api.mq.rabbitmq.encapsulation.producer"})
public class RabbitProducerAutoConfiguration {
}