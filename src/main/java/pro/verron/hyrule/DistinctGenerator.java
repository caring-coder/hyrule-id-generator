package pro.verron.hyrule;

import java.util.Iterator;

class DistinctGenerator implements IdGenerator {

    private final Iterator<Id> iterator;

    DistinctGenerator(Iterator<Id> iterator) {
        this.iterator = new RawGenerator(iterator)
                .stream()
                .distinct()
                .iterator();
    }

    @Override
    public Iterator<Id> iterator() {
        return iterator;
    }
}
