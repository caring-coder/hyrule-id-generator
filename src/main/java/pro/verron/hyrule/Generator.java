package pro.verron.hyrule;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.*;
import static java.util.Spliterators.spliteratorUnknownSize;

/**
 * A generator is an iterator that can be converted to a stream.
 *
 * @param <T> the type of the elements of the stream
 */
public record Generator<T>(Iterator<T> iterator) {

    /**
     * @return a stream of the elements of this iterator
     */
    public Stream<T> stream() {
        var spliterator = spliteratorUnknownSize(iterator, ORDERED + NONNULL + IMMUTABLE);
        return StreamSupport.stream(spliterator, false);
    }
}
