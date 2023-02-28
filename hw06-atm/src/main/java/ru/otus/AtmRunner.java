package ru.otus;

import java.util.List;

public class AtmRunner {
    public static void main(String[] args) {
        Atm atm = new Atm();
        atm.addCell(1000, 8);
        atm.addCell(5000, 10);
        atm.addCell(100, 7);
        atm.addCell(2000, 5);
        atm.addCell(500, 15);

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
