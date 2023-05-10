package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.PersistenceCreator;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "phone")
public class Phone {
    @Id
    @Nonnull
    private Long id;

    @Nonnull
    private String number;

    @Nonnull
    private String clientId;

    @PersistenceCreator
    public Phone(Long id, String number, String clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public Phone(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
