package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Random;

class RandomIdIterator implements Iterator<Id> {

    private final int nbChar;
    private final Random random;
    private final int upperBound;

    public RandomIdIterator(int nbChar, Random random, int upperBound) {
        this.nbChar = nbChar;
        this.random = random;
        this.upperBound = upperBound;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

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
