package pro.verron.hyrule;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Random;

/**
 * This class is an iterator of random ids.
 * It will generate random ids of the given size, and will not generate twice the same id.
 */
public class RandomIdIterator implements Iterator<Id> {
    private final int nbChar;
    private final Random random;
    private final int upperBound;

    /**
     * Return a random id generator, where the ids have as many characters as requested.
     *
     * @param nbChar the number of characters of the ids to generate
     * @param random the random generator to use
     * @throws AssertionError if the number of characters is not positive
     */
    public RandomIdIterator(int nbChar, Random random) {
        if (nbChar < 1)
            throw new AssertionError("Only positive upper bound is being considered");
        int upperBound = computeHighestPossibleValue(nbChar);
        this.nbChar = nbChar;
        this.random = random;
        this.upperBound = upperBound;
    }

    /**
     * This method will generate a random id generator.
     * It will generate random ids of the given size, and will not generate twice the same id.
     *
     * @param nbChar the size of the ids to generate
     * @param random the random generator to use
     * @return a generator of random ids
     */
    public static Generator<Id> generator(int nbChar, SecureRandom random) {
        Iterator<Id> randomIdIterator = new RandomIdIterator(nbChar, random);
        Iterator<Id> distinctIdIterator = new Generator<>(randomIdIterator).stream().distinct().iterator();
        return new Generator<>(distinctIdIterator);
    }

    private static int computeHighestPossibleValue(int nbChar) {
        int top = 0;
        for (int i = 0; i < nbChar; i++) {
            top = top * 10 + 9;
        }
        return top;
    }

    /**
     * This supposes that there is always a next value.
     *
     * @return always true
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    /**
     * This method will generate a random id.
     *
     * @return a random id
     */
    @Override
    public Id next() {
        /*
        TODO: this method could be considerably improved by using nextBytes()
         and working with bytes operations, it could also precompute values.
        */
        int value = random.nextInt(upperBound);
        return new Id(nbChar, value);
    }
}
