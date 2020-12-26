package com.protocolos.planner.services.impl;

import com.protocolos.planner.configurations.NotificationQueueConfig;
import com.protocolos.planner.services.WriterService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WriterServiceImpl implements WriterService {

    private final RabbitTemplate rabbitTemplate;

    public WriterServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    @Override
    public void write(String message)  {
        System.out.println("[Writer] Enviando el mensaje \"" + message + "\"...");
        rabbitTemplate.convertAndSend(NotificationQueueConfig.EXCHANGE_NOTIFICATION_NAME, NotificationQueueConfig.ROUTING_NOTIFICATION_KEY, message);
    }
}
