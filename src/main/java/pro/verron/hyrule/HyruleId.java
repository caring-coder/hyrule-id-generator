package pro.verron.hyrule;

import java.util.*;

public class HyruleId implements Comparable<HyruleId>{

    private final String format;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HyruleId hyruleId = (HyruleId) o;
        return seed == hyruleId.seed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seed);
    }

    private final int seed;

    public HyruleId(int nbChar, int seed) {
        assert nbChar >= (int)(Math.log10(seed) + 1) : "The seed should be writable with the nb of character expected";
        this.seed = seed;
        char paddingCharacter = '0';
        this.format = "%" + paddingCharacter + nbChar + "d";
    }

    public static HyruleId of(int nbChar, int seed) {
        return new HyruleId(nbChar, seed);
    }

    public String representation() {
        return String.format(format, seed);
    }

    @Override
    public int compareTo(HyruleId o) {
        return o != null ? this.seed - o.seed : -1;
    }

    @Override
    public String toString() {
        return String.format("Id{seed=%d}", seed);
    }
}
