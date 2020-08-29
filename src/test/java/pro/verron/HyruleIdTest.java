package pro.verron;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

class Hyrule_Id_Tests {

    @Test
    void should_be_able_to_create_an_hyrule_id_producer(){
        assertThat("Failed to create an HyruleId producer", HyruleId.producer(), notNullValue(Iterator.class));
    }

    @Test
    void should_be_9_characters_long_only_be_composed_of_digits(){
        HyruleId id = HyruleId.producer().next();
        assertThat(id.representation(), matchesPattern("[0-9]{9}"));
    }

    @Test
    void should_be_sequentially_different(){
        Iterator<HyruleId> producer = HyruleId.producer();
        HyruleId first = producer.next();
        HyruleId second = producer.next();
        assertThat(first, is(not(equalTo(second))));
    }

    @Test
    void should_be_equals_if_their_seed_is_the_same(){
        HyruleId first = HyruleId.of(123456789);
        HyruleId second = HyruleId.of(123456789);
        assertThat(first, is(equalTo(second)));
    }

    @Test
    void should_be_different_if_their_seed_is_different(){
        HyruleId first = HyruleId.of(123456789);
        HyruleId second = HyruleId.of(987654321);
        assertThat(first, is(not(equalTo(second))));
    }
}
