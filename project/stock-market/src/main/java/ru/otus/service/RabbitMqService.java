package ru.otus.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.otus.model.Application;

@Service
public class RabbitMqService {
    public static final String MAIN_EXCHANGE = "main-exchange";

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendApplicationCompletedEvent1(Application application) {
        rabbitTemplate.convertAndSend("investor.application_completed", application);
    }

    public void sendApplicationProcessedEvent(Application application) {
        rabbitTemplate.convertAndSend("investor.application_processed", application);
    }
}
