package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/client

    // REST сервис
    http://localhost:8080/api/clients
*/
    @SpringBootApplication
    public class WebServerRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebServerRunner.class, args);
    }
}
