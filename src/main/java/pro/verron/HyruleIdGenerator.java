package pro.verron;

import java.util.Iterator;
import java.util.Random;

class HyruleIdGenerator implements Iterator<HyruleId> {
    /**
     * By definition an Hyrule id will be between 000_000_000 and 999_999_999
     */
    private static final int HYRULE_ID_SEED_UPPER_BOUND = 1_000_000_000;

    private final Random random;

    HyruleIdGenerator(Random random) {
        this.random = random;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public HyruleId next() {
        return new HyruleId(random.nextInt(HYRULE_ID_SEED_UPPER_BOUND));
    }
}
