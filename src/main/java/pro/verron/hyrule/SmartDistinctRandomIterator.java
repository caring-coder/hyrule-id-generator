package pro.verron.hyrule;

import java.security.SecureRandom;
import java.util.*;

import static java.util.stream.Collectors.toCollection;

/// This class is an iterator of random ids.
/// It will generate random ids of the given size, and will not generate twice the same id.
public class SmartDistinctRandomIterator
        implements PrimitiveIterator.OfInt {
    private static final double SWITCH_THRESHOLD = 0.2;
    private final Random random;
    private final int maxValue;
    private final BitSet remaining;
    private int remainingCount;
    private List<Integer> remainingNumbers;

    /// Return a random id generator, where the ids have as many characters as requested.
    ///
    /// @param random the random generator to use
    ///
    /// @throws AssertionError if the number of characters is not positive
    public SmartDistinctRandomIterator(Random random, int maxValue) {
        this.random = random;
        this.maxValue = maxValue;
        var bitSet = new BitSet(maxValue);
        bitSet.set(0, maxValue);
        this.remaining = bitSet;
        this.remainingCount = maxValue;
    }

    /// This method will generate a random id generator.
    /// It will generate random ids of the given size, and will not generate twice the same id.
    ///
    /// @param random the random generator to use
    ///
    /// @return a generator of random ids
    public static PrimitiveIterator.OfInt iterator(SecureRandom random, int maxValue) {
        return new SmartDistinctRandomIterator(random, maxValue);
    }

    /// This method will generate a random id.
    ///
    /// @return a random id
    @Override
    public int nextInt() {
        if (!hasNext()) throw new NoSuchElementException("No more distinct numbers");
        if (remainingCount < maxValue * SWITCH_THRESHOLD)
            remainingNumbers = remaining();
        int candidate = remainingNumbers != null ? listPicking() : randomSampling();
        remaining.clear(candidate);
        remainingCount--;
        return candidate;
    }

    /// This supposes that there is always a next value.
    ///
    /// @return always true
    @Override
    public boolean hasNext() {
        return remainingCount > 0;
    }

    private List<Integer> remaining() {
        var list = remaining
                .stream()
                .boxed()
                .collect(toCollection(ArrayList::new));
        Collections.shuffle(list, random);
        return list;
    }

    private Integer listPicking() {
        return remainingNumbers.removeLast();
    }

    private int randomSampling() {
        while (true) {
            var candidate = random.nextInt(maxValue);
            if (remaining.get(candidate)) {
                return candidate;
            }
        }
    }
}
