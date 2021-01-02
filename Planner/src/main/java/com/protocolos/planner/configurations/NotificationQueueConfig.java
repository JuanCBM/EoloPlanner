package com.protocolos.planner.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationQueueConfig {

    public static final String EXCHANGE_NOTIFICATION_NAME = "exchange_notification_name";
    public static final String ROUTING_NOTIFICATION_KEY = "routing_notification_key";
    private static final String QUEUE_NOTIFICATION_NAME = "eoloplantCreationProgressNotifications";

    private static final boolean IS_DURABLE_QUEUE = false;

    @Bean
    Queue notificationQueue() {
        return new Queue(QUEUE_NOTIFICATION_NAME, IS_DURABLE_QUEUE);
    }

    @Bean
    TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE_NOTIFICATION_NAME);
    }

    @Bean
    Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(ROUTING_NOTIFICATION_KEY);
    }

}