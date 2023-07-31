package ru.otus.rabbitmq;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.otus.service.RabbitMqService.MAIN_EXCHANGE;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Jackson2JsonMessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        rabbitTemplate.setExchange(MAIN_EXCHANGE);
        return rabbitTemplate;
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(MAIN_EXCHANGE, false, false);
    }

    @Bean
    public Queue sellStockEventsQueue(){
        return QueueBuilder.durable("stock-sell-queue")
                .withArgument("x-dead-letter-exchange", "dead-letter-exchange")
                .build();
    }

    @Bean
    public Queue buyStockEventsQueue(){
        return QueueBuilder.durable("stock-buy-queue")
                .withArgument("x-dead-letter-exchange", "dead-letter-exchange")
                .build();
    }

    @Bean
    public Binding sellStockEventsQueueBinding(){
        return BindingBuilder.bind(sellStockEventsQueue())
                .to(topicExchange())
                .with("stock.sell");
    }

    @Bean
    public Binding buyStockEventsQueueBinding(){
        return BindingBuilder.bind(buyStockEventsQueue())
                .to(topicExchange())
                .with("stock.buy");
    }
}
