package pro.verron.hyrule;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.verron.hyrule.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class Server_Tests {

    private Server server;

    @BeforeEach
    public void before() throws IOException {
        server = new Server(new InetSocketAddress(8888));
        server.start();
    }

    @AfterEach
    public void after() {
        server.stop(1);
    }

    @Test
    void should_be_able_to_start_an_empty_server_and_get_a_response_from_it() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest query = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8888/")).build();
        HttpResponse<String> response = client.send(query, HttpResponse.BodyHandlers.ofString());
        assertThat(response.version(), is(equalTo(HttpClient.Version.HTTP_1_1)));
    }

    @Test
    void should_be_able_to_add_a_custom_context_to_a_server() throws IOException, InterruptedException {
        int responseCode = 404;
        final String customValue = "Custom Value";
        server.createContext("/", Server.respond(responseCode, () -> customValue));
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest query = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8888/")).build();
        HttpResponse<String> response = client.send(query, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode(), is(equalTo(responseCode)));
        assertThat(response.body(), is(equalTo(customValue)));
    }
}