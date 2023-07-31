package ru.otus.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "investor_id")
    private Long investorId;

    @Column(name = "code")
    private String code;

    @Column(name = "price")
    private long price;

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Stock(long id, Long investorId, String code, long price) {
        this.id = id;
        this.investorId = investorId;
        this.code = code;
        this.price = price;
    }

    public Stock() {
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", investorId='" + investorId + '\'' +
                ", code='" + code + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }

    @Override
    public boolean equals(Object obj) {

        return this.id == ((Stock) obj).id;
    }
}
