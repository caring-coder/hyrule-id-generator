package pro.verron.hyruletest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.verron.hyrule.Hyrule;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Hyrule without starting the server.
 */
public class HyruleTests {

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void before() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void should_be_able_to_get_an_id() {
        Hyrule.main(new String[]{"--help"});

        String actual = outputStream.toString(StandardCharsets.UTF_8);

        String expected = """
                Usage: <main class> [-hV] [-p=PARAM] [-s=PARAM] [--seed=PARAM] [--timeout=PARAM]
                  -h, --help            Show this help message and exit.
                  -p, --port=PARAM      listening port for http server
                  -s, --size=PARAM      size of the identifiers
                      --seed=PARAM      starting seed for the generator
                      --timeout=PARAM   waiting timeout for http server
                  -V, --version         Print version information and exit.
                """.replace("\n", System.lineSeparator());

        assertEquals(expected, actual);
    }
}
