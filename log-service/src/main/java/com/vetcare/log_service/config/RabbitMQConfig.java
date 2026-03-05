package com.vetcare.log_service.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE = "vetcare.logs.queue";
    public static final String EXCHANGE = "vetcare.exchange";
    public static final String ROUTING_KEY = "vetcare.logs.routingkey";
    
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
    
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }
    
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
    
    // JSON format eken messages yawanna meka aniwaren one
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
