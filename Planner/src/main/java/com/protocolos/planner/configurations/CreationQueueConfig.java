package com.protocolos.planner.configurations;

import com.protocolos.planner.services.impl.ReceiverServiceImpl;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreationQueueConfig {

    public static final String EXCHANGE_CREATION_NAME = "exchange_creation_name";
    public static final String ROUTING_CREATION_KEY = "routing_creation_key";
    private static final String QUEUE_CREATION_NAME = "eoloplantCreationRequests";

    private static final boolean IS_DURABLE_QUEUE = false;

    @Bean
    Queue creationQueue() {
        return new Queue(QUEUE_CREATION_NAME, IS_DURABLE_QUEUE);
    }

    @Bean
    TopicExchange creationExchange() {
        return new TopicExchange(EXCHANGE_CREATION_NAME);
    }

    @Bean
    Binding creationBinding(Queue creationQueue, TopicExchange creationExchange) {
        return BindingBuilder.bind(creationQueue).to(creationExchange).with(ROUTING_CREATION_KEY);
    }

    @Bean
    SimpleMessageListenerContainer creationContainer(ConnectionFactory connectionFactory, MessageListenerAdapter creationListenerAdapter) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_CREATION_NAME);
        container.setMessageListener(creationListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter creationListenerAdapter(ReceiverServiceImpl receiverServiceImpl) {
        return new MessageListenerAdapter(receiverServiceImpl, ReceiverServiceImpl.RECEIVE_METHOD_NAME);
    }
}