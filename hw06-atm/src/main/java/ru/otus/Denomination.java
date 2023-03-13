package ru.otus;

public enum Denomination {
    FIVE_THOUSAND(5000),
    TWO_THOUSAND(2000),
    ONE_THOUSAND(1000),
    FIVE_HUNDRED(500),
    ONE_HUNDRED(100),
    ;

    private final int value;
    Denomination(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
