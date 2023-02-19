package pro.verron.hyrule;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.util.stream.Collectors.joining;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final HttpServer server;
    private final List<HttpContext> contexts = new ArrayList<>();

    public Server(InetSocketAddress address) throws IOException {
        server = createServerBoundToAddress(address);
        createContext("catalog endpoints", "/", respond(200, () -> contexts.stream()
                .map(httpContext -> "%s \t: %s".formatted(httpContext.getPath(), httpContext.getAttributes().getOrDefault("description", "no description")))
                .collect(joining("\n"))));
        logger.info("Created server at address %s".formatted(server.getAddress()));
    }

    private static HttpServer createServerBoundToAddress(InetSocketAddress address) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(address, 0);
        return httpServer;
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

    public void createContext(String description, String path, HttpHandler handler) {
        logger.info("Created context at path %s".formatted(path));
        HttpContext context = server.createContext(path, handler);
        context.getAttributes().put("description", description);
        contexts.add(context);
    }

    public void start() {
        logger.info("Server starting...");
        server.start();
        logger.info("Server started");
    }

    public void stop(int timeout) {
        logger.info("Server stopping in %ds...".formatted(timeout));
        server.stop(timeout);
        logger.info("Server stopped");
    }
}
