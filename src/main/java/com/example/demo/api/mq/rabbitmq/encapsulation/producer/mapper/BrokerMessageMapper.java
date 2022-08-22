package com.example.demo.api.mq.rabbitmq.encapsulation.producer.mapper;

import com.example.demo.api.mq.rabbitmq.encapsulation.producer.entity.BrokerMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author wudi
 */
@Mapper
@SuppressWarnings("unused")
public interface BrokerMessageMapper {

    /**
     * 删除消息
     *
     * @param messageId 消息ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(String messageId);

    /**
     * 插入消息
     *
     * @param record 消息
     * @return 影响行数
     */
    int insert(BrokerMessage record);

    /**
     * 插入消息
     *
     * @param record 消息
     * @return 影响行数
     */
    int insertSelective(BrokerMessage record);

    /**
     * 查询消息
     *
     * @param messageId 消息ID
     * @return 消息
     */
    BrokerMessage selectByPrimaryKey(String messageId);

    /**
     * 更新消息
     *
     * @param record 消息
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(BrokerMessage record);

    /**
     * 更新消息
     *
     * @param record 消息
     * @return 影响行数
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    int updateByPrimaryKeyWithBLOBs(BrokerMessage record);

    /**
     * 更新消息
     *
     * @param record 消息
     * @return 影响行数
     */
    int updateByPrimaryKey(BrokerMessage record);

    /**
     * 改变消息状态
     *
     * @param brokerMessageId     消息ID
     * @param brokerMessageStatus 消息状态
     * @param updateTime          变更时间
     */
    void changeBrokerMessageStatus(@Param("brokerMessageId") String brokerMessageId, @Param("brokerMessageStatus") String brokerMessageStatus, @Param("updateTime") Date updateTime);

    /**
     * 查询超时消息
     *
     * @param brokerMessageStatus 消息状态
     * @return 消息列表
     */
    List<BrokerMessage> queryBrokerMessageStatus4Timeout(@Param("brokerMessageStatus") String brokerMessageStatus);

    /**
     * 按状态查询消息
     *
     * @param brokerMessageStatus 消息状态
     * @return 消息列表
     */
    List<BrokerMessage> queryBrokerMessageStatus(@Param("brokerMessageStatus") String brokerMessageStatus);

    /**
     * 更新消息重试次数
     *
     * @param brokerMessageId 消息ID
     * @param updateTime      更新时间
     * @return 影响行数
     */
    int update4TryCount(@Param("brokerMessageId") String brokerMessageId, @Param("updateTime") Date updateTime);

}