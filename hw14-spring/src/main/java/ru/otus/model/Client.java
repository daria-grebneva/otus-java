package ru.otus.model;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    public Client(String name, Address address, Set<Phone> phones) {
        this.id = null;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones) {
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
        return new Address(address.getId(), address.getStreet(), address.getClientId());
    }

    private Set<Phone> getClonedPhones(Set<Phone> phones) {
        Set<Phone> copiedPhones = new HashSet<>();
        phones.forEach(p -> copiedPhones.add(new Phone(p.getId(), p.getNumber(), p.getClientId())));
        return copiedPhones;
    }
}
