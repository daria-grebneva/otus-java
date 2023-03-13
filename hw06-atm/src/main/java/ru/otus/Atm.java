package ru.otus;

import java.util.List;

public interface Atm {
    void addCell(Denomination denomination, int quantity);

    List<Banknote> getMoneyFromAtm(int amount);

    int getAtmBalance();
}
