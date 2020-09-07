package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class Generator<T> {

    private final Iterator<T> iterator;

    public Generator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public Iterator<T> iterator(){
        return iterator;
    }

    public Stream<T> stream() {
        int characteristics = Spliterator.ORDERED
                + Spliterator.NONNULL
                + Spliterator.IMMUTABLE;
        Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator(), characteristics);
        return StreamSupport.stream(spliterator, false);
    }
}
