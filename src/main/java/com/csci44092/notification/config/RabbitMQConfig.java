package com.csci44092.notification.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for the Notification Service.
 * Configures the JSON message converter so that incoming messages from
 * the order.queue are automatically deserialized into OrderEvent objects.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Jackson-based JSON message converter for deserializing
     * incoming AMQP messages into Java objects.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configures the listener container factory to use the JSON converter.
     * This ensures @RabbitListener methods receive deserialized objects,
     * not raw byte arrays.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
