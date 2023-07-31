package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock save(Stock stock);
}
