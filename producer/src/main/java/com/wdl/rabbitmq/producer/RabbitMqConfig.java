package com.wdl.rabbitmq.producer;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * <p>Title: RabbitMqConfig.class</p>
 * <p>Description: Class Description</p>
 * <p>Copyright: Copyright (c) 2019</p>
 *
 * @author wangdali
 * @version 1.0
 * @date 2019/11/10 13:08
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue durableQueue() {
        // 声明一个名为no-loss-queue-test切标记为持久化的队列
        return new Queue("no-loss-queue-test", true);
    }

    // 声明一个名为no-loss-topexchange-test 持久化，不自动删除的交换机
    @Bean
    public TopicExchange topExchange() {
        return new TopicExchange("no-loss-topexchange-test", true, false);
    }

    @Bean
    public Binding bindingBuilder(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue)
                .to(topicExchange)
                .with("wdl.no.loss.*");
    }

}
