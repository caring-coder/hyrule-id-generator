package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Random;

class RandomIdIterator implements Iterator<Id> {

    private final int nbChar;
    private final Random random;
    private final int upperBound;

    public RandomIdIterator(int nbChar, Random random) {
        assert nbChar > 0 : "Only positive upper bound is being considered";
        int upperBound = computeHighestPossibleValue(nbChar);
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

    private static int computeHighestPossibleValue(int nbChar) {
        int top = 0;
        for(int i = 0; i < nbChar; i++){
            top = top * 10 + 9;
        }
        return top;
    }
}
