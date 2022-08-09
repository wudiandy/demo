package com.example.demo.api.mq.rabbitmq.encapsulation.api.exception;

/**
 * 运行的时候如果出现异常应该抛出RuntimeException，如果是在初始化的时候出现异常应该抛出Exception
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/9
 */
public class MessageRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1895510091786133982L;

    public MessageRuntimeException() {
        super();
    }

    public MessageRuntimeException(String message) {
        super(message);
    }

    public MessageRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRuntimeException(Throwable cause) {
        super(cause);
    }

}
