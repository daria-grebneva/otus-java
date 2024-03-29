import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.AtmImpl;
import ru.otus.Denomination;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class TestAtmImpl {
    private AtmImpl atm = null;

    @BeforeEach
    public void setUpAtm() {
        Map<Denomination, Integer> moneyMap = new HashMap<>();
        moneyMap.put(Denomination.ONE_THOUSAND, 8);
        moneyMap.put(Denomination.FIVE_THOUSAND, 10);
        moneyMap.put(Denomination.ONE_HUNDRED, 7);
        moneyMap.put(Denomination.TWO_THOUSAND, 5);
        moneyMap.put(Denomination.FIVE_HUNDRED, 15);

        atm = new AtmImpl(moneyMap);
    }

    @AfterEach
    public void tearDown() {
        atm = null;
    }

    @Test
    public void checkAtmBalanceIsWorking() {
        atm.getAtmBalance();
        assertThat(atm.getAtmBalance()).isEqualTo(76200);
    }

    @Test
    public void checkAtmBalanceIsDecreased() {
        assertThat(atm.getAtmBalance()).isEqualTo(76200);
        atm.getMoneyFromAtm(700);
        assertThat(atm.getAtmBalance()).isEqualTo(75500);
        atm.getMoneyFromAtm(1800);
        assertThat(atm.getAtmBalance()).isEqualTo(73700);
    }

    @Test
    public void checkAtmBalanceIsNotDecreasedIfExceptionWasThrown() {
        assertThat(atm.getAtmBalance()).isEqualTo(76200);
        try {
            atm.getMoneyFromAtm(750);
        } catch (RuntimeException ignored) {
        }
        assertThat(atm.getAtmBalance()).isEqualTo(76200);
    }

    @Test
    public void checkExceptionIsThrownWhenAmountIsWrong() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> atm.getMoneyFromAtm(750))
                .withMessage("There is exception with your amount in ATM");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> atm.getMoneyFromAtm(-100))
                .withMessage("There is exception with your amount in ATM");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> atm.getMoneyFromAtm(0))
                .withMessage("Initial amount can't be equal to 0");
    }

    @Test
    public void checkExceptionIsThrownWhenAtmBalanceIsNull() {
        assertThat(atm.getAtmBalance()).isEqualTo(76200);
        atm.getMoneyFromAtm(76200);
        assertThat(atm.getAtmBalance()).isEqualTo(0);
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> atm.getMoneyFromAtm(1800))
                .withMessage("There is no money in ATM");
    }

}
