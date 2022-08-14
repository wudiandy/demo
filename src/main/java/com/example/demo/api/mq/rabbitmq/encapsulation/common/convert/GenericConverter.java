package com.example.demo.api.mq.rabbitmq.encapsulation.common.convert;

import com.example.demo.api.mq.rabbitmq.encapsulation.common.serializer.Serializer;
import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import javax.annotation.Nonnull;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/14
 */
public class GenericConverter implements MessageConverter {

    private final Serializer serializer;

    public GenericConverter(Serializer serializer) {
        Preconditions.checkNotNull(serializer);
        this.serializer = serializer;
    }

    @Nonnull
    @Override
    public Message toMessage(@Nonnull Object o, @Nonnull MessageProperties messageProperties) throws MessageConversionException {
        return new Message(serializer.serializeRow(o), messageProperties);
    }

    @Nonnull
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return serializer.deserialize(message.getBody());
    }
}
