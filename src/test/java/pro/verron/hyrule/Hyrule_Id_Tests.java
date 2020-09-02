package pro.verron.hyrule;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

class Hyrule_Id_Tests {

    public static final String SEED = "HyruleDefaultIdStreamSeed";
    public static final int NB_CHAR = 9;

    @Test
    void should_be_able_to_create_an_hyrule_id_producer(){
        IdGenerator generator = Hyrule.idGenerator(NB_CHAR, SEED);
        assertThat("Failed to create an HyruleId producer", generator, is(notNullValue(IdGenerator.class)));
    }

    @Test
    void should_be_9_characters_long_only_be_composed_of_digits(){
        IdGenerator idGenerator = Hyrule.idGenerator(NB_CHAR, SEED);
        Id id = idGenerator.iterator().next();
        assertThat(id.representation(), matchesPattern("[0-9]{9}"));
    }

    @Test
    void should_be_comprised_of_its_seed_value_left_padded_by_zero(){
        Id id = Id.of(NB_CHAR, 13579);
        assertThat(id.representation(), is(equalTo("000013579")));
    }

    @Test
    void should_have_no_duplicates(){
        IdGenerator idGenerator = Hyrule.idGenerator(2, SEED);
        List<Id> list = idGenerator
                .stream()
                .limit(99)
                .collect(toList());
        Set<Id> set = new HashSet<>(list);
        assertThat("the id stream contained duplicates", set.size(), is(equalTo(list.size())));
    }

    @Test
    void should_be_equals_if_their_seed_is_the_same(){
        Id first = Id.of(NB_CHAR, 123456789);
        Id second = Id.of(NB_CHAR, 123456789);
        assertThat(first, is(equalTo(second)));
    }

    @Test
    void should_be_different_if_their_seed_is_different(){
        Id first = Id.of(NB_CHAR, 123456789);
        Id second = Id.of(NB_CHAR, 987654321);
        assertThat(first, is(not(equalTo(second))));
    }

    @Test
    void should_be_able_to_generate_a_large_number_of_ids(){
        IdGenerator idGenerator = Hyrule.idGenerator(NB_CHAR, SEED);
        Optional<Id> id = idGenerator.stream().skip(10_000).findFirst();
        assertThat("Not found that much id", id.isPresent());
    }

    @Test
    void should_not_be_ordered_ascending(){
        IdGenerator idGenerator = Hyrule.idGenerator(NB_CHAR, SEED);
        List<Id> ids = idGenerator.stream().limit(10_000).collect(toList());
        List<Id> sortedIds = ids.stream().sorted().collect(toList());
        assertThat(sortedIds, is(not(equalTo(ids))));

        List<Integer> l1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> l2 = new LinkedList<>(Arrays.asList(1, 2, 3, 4));
        assertThat(l1, is(not(equalTo(l2))));
    }

    @Test
    void should_not_be_ordered_descending(){
        Stream<Id> stream = Hyrule.idGenerator(NB_CHAR, SEED).stream();
        List<Id> ids = stream.limit(10_000).collect(toList());
        List<Id> sortedIds = ids.stream().sorted(Comparator.reverseOrder()).collect(toList());
        assertThat(sortedIds, is(not(equalTo(ids))));
    }

    @Test
    void should_reliably_get_specific_ids(){
        Id firstStream500thId = Hyrule.idGenerator(NB_CHAR, SEED).stream().skip(500).findFirst().orElseThrow();
        Id secondStream500thId = Hyrule.idGenerator(NB_CHAR, SEED).stream().skip(500).findFirst().orElseThrow();
        assertThat(firstStream500thId, is(equalTo(secondStream500thId)));
    }
}
