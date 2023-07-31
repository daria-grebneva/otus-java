package ru.otus.api;

import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Stock;
import ru.otus.model.Application;
import ru.otus.service.InvestorService;

import java.util.List;

@RestController
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    @GetMapping("/investor/{investorId}/stocks")
    public String getAllStocksByInvestor(@PathVariable("investorId") String investorId) {
        List<Stock> stocks = investorService.findAllStockByInvestorId(Long.parseLong(investorId));
        return stocks.toString();
    }

    @PostMapping("/investor/sell")
    public String sellStock(@RequestParam("stockId") String stockId, @RequestParam("investorId") String investorId) {
        Stock stock = investorService.getStockById(Long.parseLong(stockId));

        if (stock != null) {
            Application application = new Application(Long.parseLong(investorId), stock);
            investorService.sellStock(application);
        }

        return investorService.findAllStockByInvestorId(Long.parseLong(investorId)).toString();
    }

    @PostMapping("/investor/buy")
    public String buyStock(@RequestParam("stockId") String stockId, @RequestParam("investorId") String investorId) {
        Stock stock = investorService.getStockById(Long.parseLong(stockId));

        if (stock != null) {
            Application application = new Application(Long.parseLong(investorId), stock);
            investorService.buyStock(application);
        }
        return investorService.findAllStockByInvestorId(Long.parseLong(investorId)).toString();
    }
}
