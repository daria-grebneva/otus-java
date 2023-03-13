package ru.otus;

public class AtmCell {
    private final Banknote banknote;
    private int banknoteQuantity;

    public AtmCell(Denomination banknoteDenomination, int banknoteQuantity) {
        this.banknote = new Banknote(banknoteDenomination);
        this.banknoteQuantity = banknoteQuantity;
    }

    public int getBanknoteQuantity() {
        return banknoteQuantity;
    }

    public void setBanknoteQuantity(int banknoteQuantity) {
        this.banknoteQuantity = banknoteQuantity;
    }

    public Denomination getBanknoteDenomination() {
        return banknote.getDenomination();
    }

    public Banknote getBanknote() {
        return banknote;
    }
}
