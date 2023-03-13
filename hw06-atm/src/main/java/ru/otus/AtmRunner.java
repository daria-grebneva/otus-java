package ru.otus;

import java.util.List;

public class AtmRunner {
    public static void main(String[] args) {
        AtmImpl atm = new AtmImpl();
        atm.addCell(Denomination.ONE_THOUSAND, 8);
        atm.addCell(Denomination.FIVE_THOUSAND, 10);
        atm.addCell(Denomination.ONE_HUNDRED, 7);
        atm.addCell(Denomination.TWO_THOUSAND, 5);
        atm.addCell(Denomination.FIVE_HUNDRED, 15);

        System.out.println("ATM balance " + atm.getAtmBalance());

        List<Banknote> money1 = atm.getMoneyFromAtm(700);
        System.out.println(money1);
        List<Banknote> money2 = atm.getMoneyFromAtm(1500);
        System.out.println(money2);

        System.out.println("ATM balance " + atm.getAtmBalance());

        try {
            atm.getMoneyFromAtm(1550);
        } catch (RuntimeException e) {
            System.out.println(e);
        }

        System.out.println("ATM balance " + atm.getAtmBalance());
    }
}
