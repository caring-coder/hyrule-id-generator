package pro.verron;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class HyruleIdGenerator implements Iterator<HyruleId> {
    int currentSeed = 0;

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public HyruleId next() {
        return new HyruleId(currentSeed++);
    }
}
