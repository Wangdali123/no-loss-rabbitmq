package com.wdl.rabbitmq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

/**
 * <p>Title: ProducerManager.class</p>
 * <p>Description: Class Description</p>
 * <p>Copyright: Copyright (c) 2019</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2019/11/10 15:47
 */
@Service
public class ProducerManager {

    private Logger logger = LoggerFactory.getLogger(ProducerManager.class);
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange topicExchange;

    @PostConstruct
    public void init() {
        // 设置托管，默认false 如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息
        rabbitTemplate.setMandatory(true);
        /**
         * 设置接收确认回调函数
         *  1) Mandatory 需要先设置成true，否则回调失效
         *  2) exchange不存在时，不会回调
         *  3）routingKey不匹配时，replyCode返回312
         */
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                logger.info("【ReturnCallback】 拒绝接收的消息：{} replyCode:{} replyText:{} exchange:{} routingKey:{}", new String(message.getBody()), replyCode, exchange, routingKey);
            }
        });
        /**
         * 设置确认回调函数
         * 1) 队列接收到返回true,反之返回false
         * 2) 当消费者拒绝确认时，也是返回true(确认：P->Q 过程)
         * 3) exchange不存在时，返回false
         * 4) routingKey不匹配时，返回true
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                logger.info("【ConfirmCallback】 确认的消息：{} ack:{} cause:{}", correlationData, ack, cause);
            }
        });
        IntStream.range(0, 10).forEach(i -> {
            sendConfirmMsg(topicExchange.getName(), "wdl.no.loss.TEST_" + i, "测试发布者确认消息：" + i);
        });
    }

    public void sendConfirmMsg(String exchangeName, String routingKey, String msg) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey,  msg);
    }

}
