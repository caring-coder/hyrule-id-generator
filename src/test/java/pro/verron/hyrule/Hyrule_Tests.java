package pro.verron.hyrule;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class Hyrule_Tests {

    private Thread thread;

    @BeforeEach
    public void before(){
        thread = new Thread(() -> Hyrule.main(new String[0]));
        thread.start();
    }

    @AfterEach
    @Timeout(30)
    public void after() throws InterruptedException, IOException {
        HttpResponse<String> response = sendGetRequest("http://localhost:8888/kill/");
        assertThat(response.body(), is(equalTo("Unlock thread")));
        //This join call let's the test fails when it dying in time longer than the timeout in annotation
        thread.join();
    }

    public static HttpResponse<String> sendGetRequest(String s) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest query = HttpRequest.newBuilder().GET().uri(URI.create(s)).build();
        return client.send(query, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    @Timeout(value=10)
    void should_be_able_to_get_an_id() throws IOException, InterruptedException {
        HttpResponse<String> response = sendGetRequest("http://localhost:8888/hyrule/new-id/");
        String firstId = "783294182";
        assertThat(response.body(), is(equalTo(firstId)));
    }

}
