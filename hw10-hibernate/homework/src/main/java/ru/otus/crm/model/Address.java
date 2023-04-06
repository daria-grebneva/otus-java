package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@Data
@Entity
@Table(name = "address")
public class Address  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "street")
    private String street;

    @OneToOne(mappedBy = "address")
    private Client client;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }

//    @Override
//    public Address clone() {
//        return new Address(this.id, this.street);
//    }
}
