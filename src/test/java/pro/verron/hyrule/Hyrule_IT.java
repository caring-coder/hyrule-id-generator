package pro.verron.hyrule;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Hyrule with a running server.
 */
public class Hyrule_IT {
    private Thread thread;

    public static HttpResponse<String> sendGetRequest(String s) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest query = HttpRequest.newBuilder().GET().uri(URI.create(s)).build();
        return client.send(query, ofString());
    }

    @BeforeEach
    @Timeout(10)
    public void before() throws InterruptedException {
        thread = new Thread(() -> Hyrule.main(new String[0]));
        thread.start();
        Thread.sleep(100);
    }

    @AfterEach
    @Timeout(20)
    public void after() throws InterruptedException, IOException {
        HttpResponse<String> response = sendGetRequest("http://localhost:8888/kill");
        assertEquals(response.body(), "Unlock thread");
        //This join call lets the test fails when it's dying in time longer than the timeout in annotation
        thread.join(10_000);
    }

    @Test
    @Timeout(10)
    void should_be_able_to_get_an_id() throws IOException, InterruptedException {
        HttpResponse<String> response = sendGetRequest("http://localhost:8888/hyrule/new-id");
        String firstId = "783294182";
        assertEquals(response.body(), firstId);
    }
}
