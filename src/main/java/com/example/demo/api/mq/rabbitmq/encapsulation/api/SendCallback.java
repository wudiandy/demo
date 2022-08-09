package com.example.demo.api.mq.rabbitmq.encapsulation.api;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/9
 */
public interface SendCallback {

    /**
     * callback on success
     */
    void onSuccess();

    /**
     * callback on failure
     */
    void onFailure();
}
