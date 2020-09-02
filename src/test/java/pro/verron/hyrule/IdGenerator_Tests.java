package pro.verron.hyrule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

class IdGenerator_Tests {

    public static final String SEED = "HyruleDefaultIdStreamSeed";
    public static final int NB_CHAR = 9;

    private IdGenerator generator;

    @BeforeEach
    public void before() throws NoSuchAlgorithmException {
        generator = newGenerator();
    }

    private IdGenerator newGenerator() throws NoSuchAlgorithmException {
        return Hyrule.idGenerator(NB_CHAR, SEED);
    }

    @Test
    void should_be_able_to_create_an_hyrule_id_producer(){
        assertThat("Failed to create an HyruleId producer", generator, is(notNullValue(IdGenerator.class)));
    }

    @Test
    void should_be_9_characters_long_only_be_composed_of_digits(){
        Id id = generator.iterator().next();
        assertThat(id.representation(), matchesPattern("[0-9]{9}"));
    }

    @Test
    void should_have_no_duplicates() throws NoSuchAlgorithmException {
        IdGenerator smallGenerator = Hyrule.idGenerator(2, SEED);
        List<Id> list = smallGenerator
                .stream()
                .limit(99)
                .collect(toList());
        Set<Id> set = new HashSet<>(list);
        assertThat("the id stream contained duplicates", set.size(), is(equalTo(list.size())));
    }

    @Test
    void should_be_able_to_generate_a_large_number_of_ids(){
        Optional<Id> id = generator.stream().skip(10_000).findFirst();
        assertThat("Not found that much id", id.isPresent());
    }

    @Test
    void should_not_be_ordered_ascending(){
        List<Id> ids = generator.stream().limit(10_000).collect(toList());
        List<Id> sortedIds = ids.stream().sorted().collect(toList());
        assertThat(sortedIds, is(not(equalTo(ids))));
    }

    @Test
    void should_not_be_ordered_descending(){
        List<Id> ids = generator.stream().limit(10_000).collect(toList());
        List<Id> sortedIds = ids.stream().sorted(Comparator.reverseOrder()).collect(toList());
        assertThat(sortedIds, is(not(equalTo(ids))));
    }

    @Test
    void should_reliably_get_specific_ids() throws NoSuchAlgorithmException {
        Id firstStream500thId = generator.stream().skip(500).findFirst().orElseThrow();
        Id secondStream500thId = newGenerator().stream().skip(500).findFirst().orElseThrow();
        assertThat(firstStream500thId, is(equalTo(secondStream500thId)));
    }

}
