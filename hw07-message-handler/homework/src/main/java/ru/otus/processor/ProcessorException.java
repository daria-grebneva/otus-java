package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorException implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new RuntimeException("The best odd time to throw an exception");
        }
        return message;
    }
}
