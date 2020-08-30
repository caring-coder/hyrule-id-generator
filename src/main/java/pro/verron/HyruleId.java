package pro.verron;

import java.util.*;

public class HyruleId implements Comparable<HyruleId>{

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

    public HyruleId(int seed) {
        this.seed = seed;
    }

    public static HyruleId of(int seed) {
        return new HyruleId(seed);
    }

    public String representation() {
        return String.format("%9d", seed).replace(' ', '0');
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
