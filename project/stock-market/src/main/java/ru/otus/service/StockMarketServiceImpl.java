package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.domain.Stock;
import ru.otus.repository.StockRepository;

@Service
public class StockMarketServiceImpl implements StockMarketService{
    private final StockRepository stockRepository;

    public StockMarketServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Stock editStock(Stock stock) {
        return stockRepository.save(stock);
    }
}
