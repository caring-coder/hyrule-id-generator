package pro.verron;

import java.util.Iterator;
import java.util.Random;

class HyruleIdGenerator implements Iterator<HyruleId> {

    private final int nbChar;
    private final int upperBound;
    private final Random random;

    HyruleIdGenerator(int nbChar, Random random) {
        assert nbChar > 0 : "Only positive upper bound is being considered";
        this.upperBound = computeHighestPossibleValue(nbChar);
        this.nbChar = nbChar;
        this.random = random;
    }

    private int computeHighestPossibleValue(int nbChar) {
        int top = 0;
        for(int i = 0; i < nbChar; i++){
            top = top * 10 + 9;
        }
        return top;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public HyruleId next() {
        return new HyruleId(nbChar, random.nextInt(upperBound));
    }
}
