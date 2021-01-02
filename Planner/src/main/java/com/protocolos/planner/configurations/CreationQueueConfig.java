package com.protocolos.planner.configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreationQueueConfig {

    public static final String QUEUE_CREATION_NAME = "eoloplantCreationRequests";

    private static final boolean IS_DURABLE_QUEUE = false;

    @Bean
    Queue creationQueue() {
        return new Queue(QUEUE_CREATION_NAME, IS_DURABLE_QUEUE);
    }
}