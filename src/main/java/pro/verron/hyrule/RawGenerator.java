package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Random;

class RawGenerator implements IdGenerator {

    private final Iterator<Id> iterator;

    RawGenerator(int nbChar, int upperBound, Random random) {
        this.iterator = new RandomIdIterator(nbChar, random, upperBound);
    }

    @Override
    public Iterator<Id> iterator() {
        return iterator;
    }

}
