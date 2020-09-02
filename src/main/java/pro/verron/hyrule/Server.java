package pro.verron.hyrule;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.function.Supplier;
import java.util.logging.Logger;

class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final HttpServer server;

    @SneakyThrows
    public Server(InetSocketAddress address) {
        server = HttpServer.create();
        server.bind(address, 0);
    }

    public static HttpHandler respond(int responseCode, Supplier<String> bodySupplier) {
        return exchange -> {
            String responseBody = bodySupplier.get();
            byte[] responseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseCode, responseBodyBytes.length);
            OutputStream stream = exchange.getResponseBody();
            stream.write(responseBodyBytes);
            stream.flush();
            stream.close();
        };
    }

    public void createContext(String path, HttpHandler handler) {
        server.createContext(path, handler);
    }

    @SneakyThrows
    public void start() {
        logger.info(() -> MessageFormat.format("Created server at address {0}", server.getAddress()));
        logger.info("Trying to start server");
        server.start();
        logger.info("Started server");
    }

    public void stop(int timeout) {
        logger.info("Trying to stop server");
        server.stop(timeout);
        logger.info("Stopped server");
    }

}
