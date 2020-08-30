package pro.verron;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Hyrule {

    public static Stream<HyruleId> idStream() {
        Iterator<HyruleId> iterator = new HyruleIdGenerator();
        int characteristics = Spliterator.ORDERED
                + Spliterator.DISTINCT
                + Spliterator.NONNULL
                + Spliterator.NONNULL
                + Spliterator.IMMUTABLE;
        Spliterator<HyruleId> spliterator = Spliterators.spliteratorUnknownSize(iterator, characteristics);
        return StreamSupport.stream(spliterator, false);
    }

}
