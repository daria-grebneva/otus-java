package ru.otus;

public class Banknote {
    private final int denomination;

    public Banknote(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Banknote banknote = (Banknote) o;

        return getDenomination() == banknote.getDenomination();
    }

    @Override
    public int hashCode() {
        return getDenomination();
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "denomination=" + denomination +
                '}';
    }
}
