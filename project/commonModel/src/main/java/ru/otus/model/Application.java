package ru.otus.model;

import ru.otus.domain.Stock;

public class Application {
    public Application() {
    }

    public Application(long investorId, Stock stock) {
        this.investorId = investorId;
        this.stock = stock;
    }

    private long investorId;
    private Stock stock;

    public long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(long investorId) {
        this.investorId = investorId;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
