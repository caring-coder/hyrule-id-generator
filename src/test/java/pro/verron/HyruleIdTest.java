package pro.verron;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HyruleIdTest {

    @Test
    void should_be_able_to_create_an_hyrule_id_producer(){
        assertNotNull(HyruleId.producer(), "Failed to create an HyruleId producer");
    }

}
