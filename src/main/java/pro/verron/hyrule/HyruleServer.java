package pro.verron.hyrule;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HyruleServer {

    private final Iterator<HyruleId> idIterator;
    private final InetSocketAddress address;

    private HyruleServer(int nbChar, String seed, int port){
        idIterator = Hyrule.idStream(nbChar, seed).iterator();
        address = new InetSocketAddress(port);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HyruleServer hyruleServer = new HyruleServer(9, "Hyrule", 8888);
        hyruleServer.run(10);
    }

    private void run(int timeout) throws InterruptedException, IOException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);
        HttpServer server = HttpServer.create(address, 0);
        server.createContext("/hyrule/kill", this.kill(latch, timeout));
        server.createContext("/hyrule/new-id", this::newId);
        server.setExecutor(executor);
        server.start();
        latch.await();
        executor.awaitTermination(timeout / 2, TimeUnit.SECONDS);
        executor.shutdown();
        server.stop(timeout / 2);
    }

    private HttpHandler kill(CountDownLatch latch, int timeout) {
        return (exchange) -> {
            respond(exchange, 200, String.format("Killing the server in %ds", timeout));
            latch.countDown();
        };
    }

    private void newId(HttpExchange exchange) throws IOException {
        respond(exchange, 200, idIterator.next().representation());
    }

    private void respond(HttpExchange exchange, int responseCode, String responseBody) throws IOException {
        byte[] responseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(responseCode, responseBodyBytes.length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(responseBodyBytes);
        stream.flush();
        stream.close();
    }
}
