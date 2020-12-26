package com.protocolos.planner;

import com.protocolos.planner.configurations.CreationQueueConfig;
import com.protocolos.planner.configurations.NotificationQueueConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PlannerApplication implements CommandLineRunner {

    private static final String MESSAGE = "Madrid";

    @Autowired
    RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(PlannerApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        System.out.println("[Application] Enviando el mensaje \"" + MESSAGE + "\"...");
        rabbitTemplate.convertAndSend(CreationQueueConfig.EXCHANGE_CREATION_NAME, CreationQueueConfig.ROUTING_CREATION_KEY, MESSAGE);
    }
}