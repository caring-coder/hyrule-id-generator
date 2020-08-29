package pro.verron;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
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

}
