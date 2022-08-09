package com.example.demo.api.mq.rabbitmq.encapsulation.api;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/9
 */
public interface MessageListener {

    /**
     * lister on message
     *
     * @param message the listened message
     */
    void onMessage(Message message);
}
