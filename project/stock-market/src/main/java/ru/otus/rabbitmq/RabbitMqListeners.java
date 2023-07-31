package ru.otus.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.otus.domain.Stock;
import ru.otus.model.Application;
import ru.otus.service.RabbitMqService;
import ru.otus.service.StockMarketService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RabbitMqListeners {

    private final RabbitMqService rabbitMqService;
    private final StockMarketService stockMarketService;

    private Map<Stock, Long> stocksToCell = new HashMap<>();
    private Map<Stock, Long> stocksToBuy = new HashMap<>();

    public RabbitMqListeners(RabbitMqService rabbitMqService, StockMarketService stockMarketService) {
        this.rabbitMqService = rabbitMqService;
        this.stockMarketService = stockMarketService;
    }

    @RabbitListener(queues = "stock-sell-queue", ackMode = "NONE")
    public void stockSellEventsQueueListener(Application application,
                                             Channel channel,
                                             @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {

        Stock stock = application.getStock();
        Long investorId = checkIfDealSellCanBeDone(application);
        if (investorId != null) {
            stock.setInvestorId(application.getInvestorId());
            stockMarketService.editStock(stock);
            rabbitMqService.sendApplicationCompletedEvent1(application);
            return;
        }

        rabbitMqService.sendApplicationProcessedEvent(application);
    }

    @RabbitListener(queues = "stock-buy-queue", ackMode = "NONE")
    public void stockBuyEventsQueueListener(Application application,
                                              Channel channel,
                                              @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {

        Stock stock = application.getStock();
        Long investorId = checkIfDealBuyCanBeDone(application);
        if (investorId != null) {
            stock.setInvestorId(application.getInvestorId());
            stockMarketService.editStock(stock);
            rabbitMqService.sendApplicationCompletedEvent1(application);
        }

        rabbitMqService.sendApplicationProcessedEvent(application);
    }

    private Long checkIfDealSellCanBeDone(Application application) {
        Stock stock = application.getStock();
        if (stocksToBuy.containsKey(stock)) {
            Long id = stocksToBuy.get(stock);
            stocksToCell.remove(application.getStock());
            return id;
        }
        stocksToCell.put(application.getStock(), application.getInvestorId());
        return null;
    }
    private Long checkIfDealBuyCanBeDone(Application application) {
        Stock stock = application.getStock();
        if (stocksToCell.containsKey(stock)) {
            Long id = stocksToCell.get(stock);
            stocksToBuy.remove(application.getStock());
            return id;
        }
        stocksToBuy.put(application.getStock(), application.getInvestorId());
        return null;
    }

}
