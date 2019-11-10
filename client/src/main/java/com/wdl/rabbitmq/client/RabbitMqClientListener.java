package com.wdl.rabbitmq.client;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>Title: RabbitMqClientList.class</p>
 * <p>Description: Class Description</p>
 * <p>Copyright: Copyright (c) 2019</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2019/11/10 17:47
 */
@Component
public class RabbitMqClientListener {

    private Logger logger = LoggerFactory.getLogger(RabbitMqClientListener.class);
    
    @RabbitListener(queues = "no-loss-queue-test")
    @RabbitHandler
    public void testClient(Channel channel, Message message, @Headers Map<String, Object> headers) throws IOException {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        logger.info("【RabbitMq 接收的消息】 receiveMsg：{}", msg);
        long deliveryType = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicNack(deliveryType, false, true);
    }

}
