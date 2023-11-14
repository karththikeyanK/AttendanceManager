package com.attendance.Manager.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.checkin.name}")
    private String checkinQueue;

    @Value("${rabbitmq.queue.checkout.name}")
    private String CheckoutQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.checkin.key}")
    private String routingCheckinKey;

    @Value("${rabbitmq.routing.checkout.key}")
    private String routingCheckOutKey;


    @Bean
    public Queue messageCheckinQueue() {
        return new Queue(checkinQueue);
    }

    @Bean
    public Queue messageCheckoutQueue() {
        return new Queue(CheckoutQueue);
    }




    @Bean
    public TopicExchange messageExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(messageCheckinQueue()).to(messageExchange()).with(routingCheckinKey);
    }

    @Bean
    public Binding bindingJson(){
        return BindingBuilder.bind(messageCheckoutQueue()).to(messageExchange()).with(routingCheckOutKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }


}
