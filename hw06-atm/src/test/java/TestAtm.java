import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.Atm;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class TestAtm {
    private Atm atm = null;

    @BeforeEach
    public void setUpAtm() {
        atm = new Atm();
        atm.addCell(1000, 8);
        atm.addCell(5000, 10);
        atm.addCell(100, 7);
        atm.addCell(2000, 5);
        atm.addCell(500, 15);
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
