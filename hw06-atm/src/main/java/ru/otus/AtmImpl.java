package ru.otus;

import java.util.*;

public class AtmImpl implements Atm {
    private final MoneyVault moneyVault;

    public AtmImpl() {
        moneyVault = new MoneyVault();
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
        return getMoneyFromAtmRecursively(new ArrayList<>(), amount);
    }

    private List<Banknote> getMoneyFromAtmRecursively(List<Banknote> money, int resultAmount) {
        if (resultAmount == 0) {
            return money;
        } else if (resultAmount < 0) {
            recoverMoneyBack(money);
            throw new RuntimeException("There is exception with your amount in ATM");
        }
        if (getAtmBalance() <= 0) {
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
        for (AtmCell cell : moneyVault.getAtmCellList()) {
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
            moneyVault.getAtmCellList().stream()
                    .filter(c -> c.getBanknote().equals(banknote))
                    .findFirst()
                    .ifPresent(cell -> cell.setBanknoteQuantity(cell.getBanknoteQuantity() + 1));
        }
    }

    private Banknote getClosestToAmountBanknote(int amount) {
        Banknote tempBanknote = null;
        for (AtmCell cell : moneyVault.getAtmCellList()) {
            if (cell.getBanknoteDenomination().getValue() <= amount && cell.getBanknoteQuantity() > 0) {
                tempBanknote = cell.getBanknote();
            }
        }
        return tempBanknote;
    }

    @Override
    public int getAtmBalance() {
        return moneyVault.getMoneyVaultBalance();
    }
}
