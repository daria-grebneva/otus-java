package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "address")
public class Address  {

    @Id
    @Nonnull
    private Long id;

    @Nonnull
    private String street;

    @Nonnull
    private String clientId;

    @PersistenceCreator
    public Address(Long id, String street, String clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    public Address(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
