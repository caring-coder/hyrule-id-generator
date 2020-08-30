package pro.verron;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class HyruleId {

    public static Stream<HyruleId> stream() {
        Iterator<HyruleId> iterator = producer();
        int characteristics = Spliterator.ORDERED
                + Spliterator.DISTINCT
                + Spliterator.NONNULL
                + Spliterator.NONNULL
                + Spliterator.IMMUTABLE;
        Spliterator<HyruleId> spliterator = Spliterators.spliteratorUnknownSize(iterator, characteristics);
        return StreamSupport.stream(spliterator, false);
    }

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

    public static Iterator<HyruleId> producer() {
        return new HyruleIdIterator();
    }

    public static HyruleId of(int seed) {
        return new HyruleId(seed);
    }

    public String representation() {
        return String.format("%9d", seed).replace(' ', '0');
    }

    private static class HyruleIdIterator implements Iterator<HyruleId> {
        int currentSeed = 0;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public HyruleId next() {
            return new HyruleId(currentSeed++);
        }
    }
}
