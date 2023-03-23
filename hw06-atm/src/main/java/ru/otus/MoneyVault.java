package ru.otus;

import java.util.Comparator;
import java.util.List;
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

    public List<Banknote> getMoneyFromAtmRecursively(List<Banknote> money, int resultAmount) {
        if (resultAmount == 0) {
            return money;
        } else if (resultAmount < 0) {
            recoverMoneyBack(money);
            throw new RuntimeException("There is exception with your amount in ATM");
        }
        if (getMoneyVaultBalance() <= 0) {
            throw new RuntimeException("There is no money in ATM");
        }

        Banknote finalTempBanknote = getClosestToAmountBanknote(resultAmount);
        if (finalTempBanknote == null) {
            recoverMoneyBack(money);
            throw new RuntimeException("There is exception with your amount in ATM");
        }

        resultAmount = decreaseBanknoteQuantityAndAddItIntoList(money, finalTempBanknote, resultAmount);

        return getMoneyFromAtmRecursively(money, resultAmount);
    }

    private int decreaseBanknoteQuantityAndAddItIntoList(List<Banknote> money, Banknote finalTempBanknote, int resultAmount) {
        for (AtmCell cell : getAtmCellList()) {
            if (cell.getBanknote().equals(finalTempBanknote)) {
                money.add(finalTempBanknote);
                cell.setBanknoteQuantity(cell.getBanknoteQuantity() - 1);
                resultAmount = resultAmount - finalTempBanknote.getDenomination().getValue();
            }
        }

        return resultAmount;
    }

    private void recoverMoneyBack(List<Banknote> money) {
        for (Banknote banknote : money) {
            getAtmCellList().stream()
                    .filter(c -> c.getBanknote().equals(banknote))
                    .findFirst()
                    .ifPresent(cell -> cell.setBanknoteQuantity(cell.getBanknoteQuantity() + 1));
        }
    }

    private Banknote getClosestToAmountBanknote(int amount) {
        Banknote tempBanknote = null;
        for (AtmCell cell : getAtmCellList()) {
            if (cell.getBanknoteDenomination().getValue() <= amount && cell.getBanknoteQuantity() > 0) {
                tempBanknote = cell.getBanknote();
            }
        }
        return tempBanknote;
    }
}
