package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Random;

class DistinctGenerator implements IdGenerator {

    private final Iterator<Id> iterator;

    DistinctGenerator(int nbChar, Random random) {
        assert nbChar > 0 : "Only positive upper bound is being considered";
        int upperBound = computeHighestPossibleValue(nbChar);
        RandomIdIterator iterator = new RandomIdIterator(nbChar, random, upperBound);
        this.iterator = new RawGenerator(iterator)
                .stream()
                .distinct()
                .iterator();
    }

    private int computeHighestPossibleValue(int nbChar) {
        int top = 0;
        for(int i = 0; i < nbChar; i++){
            top = top * 10 + 9;
        }
        return top;
    }

    @Override
    public Iterator<Id> iterator() {
        return iterator;
    }
}
