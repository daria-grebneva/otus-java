package ru.otus.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
        rabbitTemplate.setReplyTimeout(15000);
        rabbitTemplate.setMessageConverter(jsonConverter());
        rabbitTemplate.setExchange(MAIN_EXCHANGE);
        return rabbitTemplate;
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(MAIN_EXCHANGE, false, false);
    }

    @Bean
    public Queue applicationCompletedEventsQueue() {
        return new Queue("application-completed-queue");
    }

    @Bean
    public Queue applicationProcessedEventsQueue() {
        return new Queue("application-processed-queue");
    }

    @Bean
    public Binding applicationCompletedEventsQueueBinding() {
        return BindingBuilder.bind(applicationCompletedEventsQueue())
                .to(topicExchange())
                .with("investor.application_completed");
    }
    @Bean
    public Binding applicationProcessedEventsQueueBinding() {
        return BindingBuilder.bind(applicationProcessedEventsQueue())
                .to(topicExchange())
                .with("investor.application_processed");
    }
}
