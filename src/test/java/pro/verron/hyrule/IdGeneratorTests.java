package pro.verron.hyrule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the id generator.
 */
class IdGeneratorTests {
    public static final String SEED = "HyruleDefaultIdStreamSeed";
    public static final int NB_CHAR = 9;
    private Generator<Id> generator;

    @BeforeEach
    public void before() throws NoSuchAlgorithmException {
        generator = newGenerator();
    }

    private Generator<Id> newGenerator() throws NoSuchAlgorithmException {
        return RandomIdIterator.generator(NB_CHAR, Hyrule.getSecureRandom(SEED));
    }

    @Test
    void should_be_able_to_create_an_hyrule_id_producer() {
        assertNotNull(generator, "Failed to create an HyruleId producer");
    }

    @Test
    void should_be_9_characters_long_only_be_composed_of_digits() {
        Id id = generator.iterator().next();
        assertTrue(id.representation().matches("[0-9]{9}"));
    }

    @Test
    void shunt_have_zero_or_negative_size_characters() {
        assertThrows(AssertionError.class, () -> RandomIdIterator.generator(0, Hyrule.getSecureRandom(SEED)));
        assertThrows(AssertionError.class, () -> RandomIdIterator.generator(-2, Hyrule.getSecureRandom(SEED)));
    }

    @Test
    void should_have_no_duplicates() throws NoSuchAlgorithmException {
        Generator<Id> smallGenerator = RandomIdIterator.generator(2, Hyrule.getSecureRandom(SEED));
        List<Id> list = smallGenerator
                .stream()
                .limit(99)
                .toList();
        Set<Id> set = new HashSet<>(list);
        assertEquals(set.size(), list.size(), "the id stream contained duplicates");
    }

    @Test
    void should_be_able_to_generate_a_large_number_of_ids() {
        Optional<Id> id = generator.stream().skip(10_000).findFirst();
        assertTrue(id.isPresent(), "Not found that much id");
    }

    @Test
    void should_not_be_ordered_ascending() {
        List<Id> ids = generator.stream().limit(10_000).collect(toList());
        List<Id> sortedIds = ids.stream().sorted().collect(toList());
        assertNotEquals(sortedIds, ids);
    }

    @Test
    void should_not_be_ordered_descending() {
        List<Id> ids = generator.stream().limit(10_000).collect(toList());
        List<Id> sortedIds = ids.stream().sorted(reverseOrder()).collect(toList());
        assertNotEquals(sortedIds, ids);
    }

    @Test
    void should_reliably_get_specific_ids() throws NoSuchAlgorithmException {
        Id firstStream500thId = generator.stream().skip(500).findFirst().orElseThrow();
        Id secondStream500thId = newGenerator().stream().skip(500).findFirst().orElseThrow();
        assertEquals(firstStream500thId, secondStream500thId);
    }

}
