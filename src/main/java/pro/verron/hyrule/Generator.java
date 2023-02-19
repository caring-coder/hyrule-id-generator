package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterators.spliteratorUnknownSize;

public record Generator<T>(Iterator<T> iterator) {
    public Stream<T> stream() {
        var spliterator = spliteratorUnknownSize(iterator,
                Spliterator.ORDERED
                        + Spliterator.NONNULL
                        + Spliterator.IMMUTABLE);
        return StreamSupport.stream(spliterator, false);
    }
}
