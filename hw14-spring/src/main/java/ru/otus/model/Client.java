package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @Nonnull
    private Long id;

    @Nonnull
    private String name;

    @Nonnull
    @MappedCollection(idColumn = "client_id")
    private Address address;

    @Nonnull
    @MappedCollection(idColumn = "client_id")
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(String name, Address address, List<Phone> phones) {
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name, getClonedAddress(this.address), getClonedPhones(this.phones));
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }

    private Address getClonedAddress(Address address) {
        return new Address(address.getId(), address.getStreet(), id.toString());
    }

    private List<Phone> getClonedPhones(List<Phone> phones) {
        List<Phone> copiedPhones = new ArrayList<>();
        phones.forEach(p -> copiedPhones.add(new Phone(p.getId(), p.getNumber(), p.getId().toString())));
        return copiedPhones;
    }
}
