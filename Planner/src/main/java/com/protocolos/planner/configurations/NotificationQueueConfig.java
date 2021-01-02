package com.protocolos.planner.configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationQueueConfig {

    public static final String QUEUE_NOTIFICATION_NAME = "eoloplantCreationProgressNotifications";

    private static final boolean IS_DURABLE_QUEUE = false;

    @Bean
    Queue notificationQueue() {
        return new Queue(QUEUE_NOTIFICATION_NAME, IS_DURABLE_QUEUE);
    }

}