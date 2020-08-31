package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Random;

class RawIterator implements Iterator<Id> {
    private final int nbChar;
    private final Random random;
    private final int upperBound;

    public RawIterator(int nbChar, Random random, int upperBound) {
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
        return new Id(nbChar, random.nextInt(upperBound));
    }
}
