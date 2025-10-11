package pro.verron.hyrule;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.StreamSupport;

import static java.util.Comparator.reverseOrder;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the id generator.
 */
class IdGeneratorTests {
    public static final String SEED = "HyruleDefaultIdStreamSeed";
    private static final SecureRandom secureRandom;

    static {
        try {
            secureRandom = Hyrule.getSecureRandom(SEED);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void should_be_able_to_create_an_hyrule_id_producer() {
        var maxValue = Math.powExact(10, 9);
        var iterator = SmartDistinctRandomIterator.iterator(secureRandom, maxValue);
        assertNotNull(iterator, "Failed to create an HyruleId producer");
    }

    @Test
    void should_be_9_characters_long_only_be_composed_of_digits() {
        var maxValue = Math.powExact(10, 9);
        var iterator = SmartDistinctRandomIterator.iterator(secureRandom, maxValue);
        Id id = new Id(9, iterator.next());
        assertTrue(id.representation()
                     .matches("[0-9]{9}"));
    }

    @Test
    void shunt_have_zero_or_negative_size_characters() {
        assertThrows(AssertionError.class, () -> new Id(0, 12345));
        assertThrows(AssertionError.class, () ->new Id(-1, 12345));
    }

    @Test
    void should_have_no_duplicates() {
        var maxValue = Math.powExact(10, 2);
        var iterator = SmartDistinctRandomIterator.iterator(secureRandom, maxValue);
        List<Integer> list = StreamSupport.stream(newSpliterator(iterator, 2), false)
                                          .limit(99)
                                          .toList();
        Set<Integer> set = new HashSet<>(list);
        assertEquals(set.size(), list.size(), "the id stream contained duplicates");
    }

    private Spliterator<Integer> newSpliterator(Iterator<Integer> iterator, int nbChar) {
        return Spliterators.spliterator(iterator, Math.powExact(10, nbChar), Spliterator.DISTINCT);
    }

    @Test
    void should_be_able_to_generate_a_large_number_of_ids() {
        var maxValue = Math.powExact(10, 9);
        var iterator = SmartDistinctRandomIterator.iterator(secureRandom, maxValue);
        var id = StreamSupport.stream(newSpliterator(iterator, 9), false)
                                            .skip(10_000)
                                            .findFirst();
        assertTrue(id.isPresent(), "Not found that much id");
    }

    @Test
    void should_not_be_ordered_ascending() {
        var maxValue = Math.powExact(10, 9);
        var iterator = SmartDistinctRandomIterator.iterator(secureRandom, maxValue);
        List<Integer> ids = StreamSupport.stream(newSpliterator(iterator, 9), false)
                                         .limit(10_000)
                                         .toList();
        List<Integer> sortedIds = ids.stream()
                                     .sorted()
                                     .toList();
        assertNotEquals(sortedIds, ids);
    }

    @Test
    void should_not_be_ordered_descending() {
        var maxValue = Math.powExact(10, 9);
        var iterator = SmartDistinctRandomIterator.iterator(secureRandom, maxValue);
        List<Integer> ids = StreamSupport.stream(newSpliterator(iterator, 9), false)
                                         .limit(10_000)
                                         .toList();
        List<Integer> sortedIds = ids.stream()
                                     .sorted(reverseOrder())
                                     .toList();
        assertNotEquals(sortedIds, ids);
    }

    @Test
    void should_reliably_get_specific_ids()
            throws NoSuchAlgorithmException {
        var maxValue = Math.powExact(10, 9);
        var iterator1 = SmartDistinctRandomIterator.iterator(Hyrule.getSecureRandom(SEED), maxValue);
        Integer firstStream500thId = StreamSupport.stream(newSpliterator(iterator1, 9), false)
                                                  .skip(500)
                                                  .findFirst()
                                                  .orElseThrow();

        var iterator2 = SmartDistinctRandomIterator.iterator(Hyrule.getSecureRandom(SEED), maxValue);
        Integer secondStream500thId = StreamSupport.stream(newSpliterator(iterator2, 9), false)
                                                   .skip(500)
                                                   .findFirst()
                                                   .orElseThrow();
        assertEquals(firstStream500thId, secondStream500thId);
    }

}
