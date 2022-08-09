package com.example.demo.api.mq.rabbitmq.encapsulation.api.exception;

/**
 * 运行的时候如果出现异常应该抛出RuntimeException，如果是在初始化的时候出现异常应该抛出Exception
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/9
 */
public class MessageException extends Exception {

    private static final long serialVersionUID = -2561449191601855971L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

}
