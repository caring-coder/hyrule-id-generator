package pro.verron.hyrule;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class Id_Tests {

    public static final int NB_CHAR = 9;

    @Test
    void should_be_comprised_of_its_seed_value_left_padded_by_zero(){
        Id id = Id.of(NB_CHAR, 13579);
        assertThat(id.representation(), is(equalTo("000013579")));
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
    void should_be_represented_by_same_string_when_identical_seed(){
        Id first = Id.of(NB_CHAR, 123456789);
        Id second = Id.of(NB_CHAR, 123456789);
        assertThat(first.toString(), is(equalTo(second.toString())));
    }

    @Test
    void should_not_be_represented_by_same_string_when_different_seed(){
        Id first = Id.of(NB_CHAR, 123456789);
        Id second = Id.of(NB_CHAR, 987654321);
        assertThat(first.toString(), is(not(equalTo(second.toString()))));
    }

    @Test
    void should_be_equals_to_itself(){
        Id one = Id.of(NB_CHAR, 123456789);
        assertThat(one, is(equalTo(one)));
    }

    @Test
    void should_not_be_equals_to_null(){
        Id one = Id.of(NB_CHAR, 123456789);
        assertThat(one, is(not(equalTo(null))));
    }

    @Test
    void should_not_be_equals_to_an_unrelated_type(){
        Id one = Id.of(NB_CHAR, 123456789);
        assertThat(one, is(not(equalTo(new Object()))));
    }
}
