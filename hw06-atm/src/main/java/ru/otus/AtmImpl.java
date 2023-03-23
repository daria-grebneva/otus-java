package ru.otus;

import java.util.*;

public class AtmImpl implements Atm {
    private final MoneyVault moneyVault;

    public AtmImpl(Map<Denomination, Integer> money) {
        moneyVault = new MoneyVault();
        money.forEach(this::addCell);
    }

    @Override
    public void addCell(Denomination denomination, int quantity) {
        AtmCell atmCell = new AtmCell(denomination, quantity);
        moneyVault.addCellToVault(atmCell);
    }

    @Override
    public List<Banknote> getMoneyFromAtm(int amount) {
        if (amount == 0) {
            throw new RuntimeException("Initial amount can't be equal to 0");
        }
        return moneyVault.getMoneyFromAtmRecursively(new ArrayList<>(), amount);
    }

    @Override
    public int getAtmBalance() {
        return moneyVault.getMoneyVaultBalance();
    }
}
