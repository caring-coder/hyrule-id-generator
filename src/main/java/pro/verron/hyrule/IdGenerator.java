package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

interface IdGenerator {

    Iterator<Id> iterator();

    default Stream<Id> stream() {
        int characteristics = Spliterator.ORDERED
                + Spliterator.NONNULL
                + Spliterator.IMMUTABLE;
        Spliterator<Id> spliterator = Spliterators.spliteratorUnknownSize(iterator(), characteristics);
        return StreamSupport.stream(spliterator, false);
    }
}
