package pro.verron.hyrule;

import java.util.Iterator;
import java.util.PrimitiveIterator;

public record IdIterator(PrimitiveIterator.OfInt iterator, int size)
        implements Iterator<Id> {
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Id next() {
        return new Id(size, iterator.next());
    }
}
