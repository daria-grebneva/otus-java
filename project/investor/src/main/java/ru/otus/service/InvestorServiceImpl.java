package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.domain.Investor;
import ru.otus.domain.Stock;
import ru.otus.model.Application;
import ru.otus.repository.StockRepository;

import java.util.List;

@Service
public class InvestorServiceImpl implements InvestorService{
    private final StockRepository stockRepository;
    private final RabbitMqService rabbitMqService;

    public InvestorServiceImpl(StockRepository stockRepository, RabbitMqService rabbitMqService) {
        this.stockRepository = stockRepository;
        this.rabbitMqService = rabbitMqService;
    }

    @Override
    public List<Investor> findAll() {
        return null;
    }

    @Override
    public void sellStock(Application application) {

        rabbitMqService.sendStockSellEvent(application);
    }


    @Override
    public void buyStock(Application application) {

        rabbitMqService.sendBuySellEvent(application);
    }

    @Override
    public List<Stock> findAllStockByInvestorId(long id) {
        return stockRepository.findByInvestorId(id);
    }

    @Override
    public Stock getStockById(long id) {
        return stockRepository.findById(id).orElse(null);
    }
}
