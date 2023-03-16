package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorExceptionTest {

    @ParameterizedTest
    @DisplayName("Тестируем выброс исключения в четную секунду")
    @ValueSource(ints = {4, 10, 20})
    void testExceptionProcessInEven(int second) {
        var message = new Message.Builder(1L).field7("field7").build();
        var processorException = new ProcessorException(() ->
                LocalDateTime.of(2023, Month.APRIL, 8, 12, 30, second));
        assertThrows(RuntimeException.class, () -> processorException.process(message));
    }
    @ParameterizedTest
    @DisplayName("Тестируем выброс исключения в нечетную секунду")
    @ValueSource(ints = {1, 5, 13})
    void testNoExceptionProcessInOdd(int second) {
        var message = new Message.Builder(1L).field7("field7").build();
        var processorException = new ProcessorException(() ->
                LocalDateTime.of(2023, Month.APRIL, 8, 12, 30, second));
        assertDoesNotThrow(() -> processorException.process(message));
    }
}