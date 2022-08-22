package com.example.demo.api.mq.rabbitmq.encapsulation.producer.constant;

/**
 * 消息的发送状态
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/22
 */
public enum BrokerMessageStatus {

    /**
     * 正在发送
     */
    SENDING("0"),

    /**
     * 发送成功
     */
    SEND_OK("1"),

    /**
     * 由于磁盘满了导致的发送失败，没有必要重试
     */
    SEND_FALL("2"),

    /**
     * 由于系统繁忙造成的失败，可以重试
     */
    SEND_FALL_A_MOMENT("3");

    private final String code;

    BrokerMessageStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
