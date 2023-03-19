package ru.otus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtmRunner {
    public static void main(String[] args) {
        Map<Denomination, Integer> moneyMap = new HashMap<>();
        moneyMap.put(Denomination.ONE_THOUSAND, 8);
        moneyMap.put(Denomination.FIVE_THOUSAND, 10);
        moneyMap.put(Denomination.ONE_HUNDRED, 7);
        moneyMap.put(Denomination.TWO_THOUSAND, 5);
        moneyMap.put(Denomination.FIVE_HUNDRED, 15);
        
        AtmImpl atm = new AtmImpl(moneyMap);

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
