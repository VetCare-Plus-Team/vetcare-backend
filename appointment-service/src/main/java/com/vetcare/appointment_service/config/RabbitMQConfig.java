package com.vetcare.appointment_service.config;


import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "vetcare.exchange";
    public static final String ROUTING_KEY = "vetcare.logs.routingkey";
    
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
