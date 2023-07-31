package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.Investor;

public interface InvestorRepository extends JpaRepository<Investor, Long> {

}
