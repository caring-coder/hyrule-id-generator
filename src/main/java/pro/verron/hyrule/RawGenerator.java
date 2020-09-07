package pro.verron.hyrule;

import java.util.Iterator;
import java.util.Random;

class RawGenerator implements IdGenerator {

    private final Iterator<Id> iterator;

    RawGenerator(Iterator<Id> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Iterator<Id> iterator() {
        return iterator;
    }

}
