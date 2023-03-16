package ru.otus;

import java.util.Comparator;
import java.util.TreeSet;

public class MoneyVault {

    private final TreeSet<AtmCell> atmCellList;

    public MoneyVault() {
        atmCellList = new TreeSet<>(Comparator.comparingInt(c -> c.getBanknoteDenomination().getValue()));
    }

    public TreeSet<AtmCell> getAtmCellList() {
        return atmCellList;
    }

    public void addCellToVault(AtmCell atmCell) {
        atmCellList.add(atmCell);
    }

    public int getMoneyVaultBalance() {
        return atmCellList.stream().mapToInt(c -> c.getBanknoteDenomination().getValue() * c.getBanknoteQuantity()).sum();
    }
}
