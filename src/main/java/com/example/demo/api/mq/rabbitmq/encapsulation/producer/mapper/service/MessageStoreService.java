package com.example.demo.api.mq.rabbitmq.encapsulation.producer.mapper.service;

import com.example.demo.api.mq.rabbitmq.encapsulation.producer.constant.BrokerMessageStatus;
import com.example.demo.api.mq.rabbitmq.encapsulation.producer.entity.BrokerMessage;
import com.example.demo.api.mq.rabbitmq.encapsulation.producer.mapper.BrokerMessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author wudi.andy@outlook.com
 * @date 2022/8/22
 */
@Service
public class MessageStoreService {

    @Resource
    private BrokerMessageMapper brokerMessageMapper;

    public int insert(BrokerMessage brokerMessage) {
        return this.brokerMessageMapper.insert(brokerMessage);
    }

    public void success(String messageId) {
        this.brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_OK.getCode(), new Date());
    }

    public void failure(String messageId) {
        this.brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_FALL.getCode(), new Date());
    }
}
