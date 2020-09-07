package pro.verron.hyrule;

import java.util.*;

public final class Id implements Comparable<Id>{

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
        if (o == null) return false;
        if (Id.class != o.getClass()) return false;
        Id id = (Id) o;
        return value == id.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(Id o) {
        return o == null
                ? -1
                : this.value - o.value;
    }

    @Override
    public String toString() {
        return String.format("Id{seed=%d}", value);
    }
}
