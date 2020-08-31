package pro.verron.hyrule;

import java.util.*;

public class Id implements Comparable<Id>{

    public static Id of(int nbChar, int seed) {
        return new Id(nbChar, seed);
    }

    private final String format;
    private final int value;

    public Id(int nbChar, int value) {
        assert nbChar >= (int)(Math.log10(value) + 1) : "The seed should be writable with the nb of character expected";
        this.value = value;
        char paddingCharacter = '0';
        this.format = "%" + paddingCharacter + nbChar + "d";
    }

    public String representation() {
        return String.format(format, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id id = (Id) o;
        return value == id.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(Id o) {
        return o != null ? this.value - o.value : -1;
    }

    @Override
    public String toString() {
        return String.format("Id{seed=%d}", value);
    }
}
