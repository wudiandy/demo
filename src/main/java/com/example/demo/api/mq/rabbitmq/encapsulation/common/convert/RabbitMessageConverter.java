package com.example.demo.api.mq.rabbitmq.encapsulation.common.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import javax.annotation.Nonnull;

/**
 * 装饰者模式，用RabbitMessageConverter装饰GenericConverter
 *
 * @author wudi.andy@outlook.com
 * @date 2022/8/14
 */
public class RabbitMessageConverter implements MessageConverter {

    private final GenericConverter delegate;

    private final String defaultExpire = String.valueOf(24 * 60 * 60 * 1000);

    public RabbitMessageConverter(GenericConverter genericConverter) {
        Preconditions.checkNotNull(genericConverter);
        this.delegate = genericConverter;
    }

    @Nonnull
    @Override
    public Message toMessage(@Nonnull Object object, MessageProperties messageProperties) throws MessageConversionException {
        // 在返回之前写一些装饰逻辑
        messageProperties.setExpiration(defaultExpire);
        return delegate.toMessage(object, messageProperties);
    }

    @Nonnull
    @Override
    public Object fromMessage(@Nonnull Message message) throws MessageConversionException {
        return delegate.fromMessage(message);
    }
}
