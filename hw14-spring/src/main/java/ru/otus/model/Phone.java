package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.PersistenceCreator;

@Table(name = "phone")
public class Phone {
    @Id
    private final Long id;

    private final Long clientId;

    private final String number;

    @PersistenceCreator
    public Phone(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public Phone(String number, Long clientId) {
        this.id = null;
        this.number = number;
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    public Long getClientId() {
        return clientId;
    }

    public String getNumber() {
        return number;
    }

    public Long getId() {
        return id;
    }
}
