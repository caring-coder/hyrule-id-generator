package pro.verron.hyruletest;

import org.junit.jupiter.api.Test;
import pro.verron.hyrule.Id;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the id.
 */
public class IdTests {
    public static final int NB_CHAR = 9;

    @Test
    void should_be_comprised_of_its_seed_value_left_padded_by_zero() {
        Id id = Id.of(NB_CHAR, 13579);
        assertEquals(id.representation(), "000013579");
    }

    @Test
    void should_be_equals_if_their_seed_is_the_same() {
        Id first = Id.of(NB_CHAR, 123456789);
        Id second = Id.of(NB_CHAR, 123456789);
        assertEquals(first, second);
    }

    @Test
    void should_be_different_if_their_seed_is_different() {
        Id first = Id.of(NB_CHAR, 123456789);
        Id second = Id.of(NB_CHAR, 987654321);
        assertNotEquals(first, second);
    }

    @Test
    void should_be_represented_by_same_string_when_identical_seed() {
        Id first = Id.of(NB_CHAR, 123456789);
        Id second = Id.of(NB_CHAR, 123456789);
        assertEquals(first.toString(), second.toString());
    }

    @Test
    void should_not_be_represented_by_same_string_when_different_seed() {
        Id first = Id.of(NB_CHAR, 123456789);
        Id second = Id.of(NB_CHAR, 987654321);
        assertNotEquals(first.toString(), second.toString());
    }

    @Test
    void should_be_equals_to_itself() {
        Id one = Id.of(NB_CHAR, 123456789);
        assertEquals(one, one);
    }

    @Test
    void should_not_be_equals_to_null() {
        Id one = Id.of(NB_CHAR, 123456789);
        assertNotEquals(one, null);
    }

    @Test
    void should_not_be_equals_to_an_unrelated_type() {
        Id one = Id.of(NB_CHAR, 123456789);
        assertNotEquals(one, new Object());
    }

    @Test
    void should_be_comparable_even_with_null_values() {
        Id one = Id.of(NB_CHAR, 1);
        Id two = Id.of(NB_CHAR, 2);
        assertTrue(two.compareTo(one) > 0);
        assertTrue(one.compareTo(two) < 0);
        assertTrue(one.compareTo(null) < 0);
    }

    @Test
    void should_be_writable_with_number_of_char_chosen() {
        assertDoesNotThrow(() -> Id.of(1, 3));
        assertDoesNotThrow(() -> Id.of(2, 3));
        assertDoesNotThrow(() -> Id.of(2, 99));
        assertThrows(AssertionError.class, () -> Id.of(1, 43));
        assertThrows(AssertionError.class, () -> Id.of(2, 3456));
    }

    @Test
    void should_have_compliant_hashcode_implementations() {
        Id firstOne = Id.of(NB_CHAR, 1);
        Id secondOne = Id.of(NB_CHAR, 1);
        Id two = Id.of(NB_CHAR, 2);
        assertEquals(firstOne.hashCode(), secondOne.hashCode());
        assertNotEquals(firstOne.hashCode(), two.hashCode());
    }
}
