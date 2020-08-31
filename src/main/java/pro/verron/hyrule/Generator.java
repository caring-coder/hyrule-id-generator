package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Random;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class Generator implements Iterator<Id> {

    private final int nbChar;
    private final int upperBound;
    private final Random random;

    Generator(int nbChar, Random random) {
        assert nbChar > 0 : "Only positive upper bound is being considered";
        this.upperBound = computeHighestPossibleValue(nbChar);
        this.nbChar = nbChar;
        this.random = random;
    }

    public Stream<Id> asStream() {
        int characteristics = Spliterator.ORDERED
                + Spliterator.NONNULL
                + Spliterator.IMMUTABLE;
        Spliterator<Id> spliterator = Spliterators.spliteratorUnknownSize(this, characteristics);
        return StreamSupport.stream(spliterator, false).distinct();
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
    public Id next() {
        return new Id(nbChar, random.nextInt(upperBound));
    }
}
