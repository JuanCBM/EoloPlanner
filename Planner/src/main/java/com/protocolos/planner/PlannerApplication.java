package com.protocolos.planner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protocolos.planner.configurations.CreationQueueConfig;
import com.protocolos.planner.configurations.NotificationQueueConfig;
import com.protocolos.planner.models.City;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class PlannerApplication implements CommandLineRunner {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(PlannerApplication.class, args);
    }

    @Override
    public void run(String... args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        City city = City.builder().city("Madrid").id(1L).build();
        String message = mapper.writeValueAsString(city);
        System.out.println("[Application] Enviando el mensaje \"" + message + "\"...");

        rabbitTemplate.convertAndSend(CreationQueueConfig.QUEUE_CREATION_NAME, message);

    }
}