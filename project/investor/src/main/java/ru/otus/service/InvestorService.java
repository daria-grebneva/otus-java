package ru.otus.service;

import ru.otus.domain.Investor;
import ru.otus.domain.Stock;
import ru.otus.model.Application;

import java.util.List;

public interface InvestorService {
    List<Investor> findAll();

    void sellStock(Application application);
    void buyStock(Application application);

    List<Stock> findAllStockByInvestorId(long id);
    Stock getStockById(long id);
}
