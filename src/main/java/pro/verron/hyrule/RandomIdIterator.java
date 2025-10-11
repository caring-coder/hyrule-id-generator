package pro.verron.hyrule;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Random;

/// This class is an iterator of random ids.
/// It will generate random ids of the given size, and will not generate twice the same id.
public class RandomIdIterator
        implements Iterator<Id> {
    private final int size;
    private final Random random;
    private final int upperBound;

    /// Return a random id generator, where the ids have as many characters as requested.
    ///
    /// @param random the random generator to use
    /// @param size   the size of the ids to generate
    /// @param base   the number of signs in the numeric base
    ///
    /// @throws AssertionError if the number of characters is not positive
    public RandomIdIterator(Random random, int size, int base) {
        if (size < 1)
            throw new AssertionError("Only positive upper bound is being considered");
        this.size = size;
        this.random = random;
        this.upperBound = computeHighestPossibleValue(size, base);
    }

    private static int computeHighestPossibleValue(int size, int base) {
        return base * size - 1;
    }

    /// This method will generate a random id generator.
    /// It will generate random ids of the given size, and will not generate twice the same id.
    ///
    /// @param random the random generator to use
    /// @param size   the size of the ids to generate
    ///
    /// @return a generator of random ids
    public static Generator<Id> generator(SecureRandom random, int size, int base) {
        Iterator<Id> randomIdIterator = new RandomIdIterator(random, size, base);
        Iterator<Id> distinctIdIterator = new Generator<>(randomIdIterator).stream()
                                                                           .distinct()
                                                                           .iterator();
        return new Generator<>(distinctIdIterator);
    }

    /// This supposes that there is always a next value.
    ///
    /// @return always true
    @Override
    public boolean hasNext() {
        return true;
    }

    /// This method will generate a random id.
    ///
    /// @return a random id
    @Override
    public Id next() {
        // TODO: method can be improved by using nextBytes(), bytes operations, and precomputing values
        int value = random.nextInt(upperBound);
        return new Id(size, value);
    }
}
