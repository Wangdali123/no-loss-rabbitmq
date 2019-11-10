package com.wdl.rabbitmq.producer;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

/**
 * <p>Title: TestController.class</p>
 * <p>Description: Class Description</p>
 * <p>Copyright: Copyright (c) 2019</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2019/11/10 15:57
 */
@RestController
public class TestController {

    @Autowired
    private TopicExchange topicExchange;

    @Autowired
    private ProducerManager producerManager;

    @GetMapping("template/param")
    public String getRabbitMqTemplateParam() {
        producerManager.sendConfirmMsg(topicExchange.getName(), "wdl.no.loss.TEST_ADMIN", "测试发布者确认消息：ADMIN");
        return "调用成功：" + LocalTime.now().toString();
    }

}
