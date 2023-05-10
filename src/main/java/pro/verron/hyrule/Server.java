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

/**
 * This represents a web server that can be configured to serve different endpoints.
 * <p>
 * It is based on the {@link HttpServer} class from the JDK.
 */
public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private final HttpServer server;
    private final List<HttpContext> contexts = new ArrayList<>();

    /**
     * Create a server bound to the given address.
     *
     * @param address the address to bind the server to.
     * @throws IOException if the server cannot be created.
     */
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

    /**
     * Create a handler that will respond with the given response code and body.
     *
     * @param responseCode the response code to use.
     *                     It must be a valid HTTP response code.
     *                     See <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Status">the list of valid codes</a>.
     * @param bodySupplier the supplier of the body to use.
     *                     The body will be encoded in UTF-8.
     *                     The body will be sent as is, as a single chunk, so it must be encoded if necessary.
     * @return the handler that will respond with the given response code and body.
     */
    public static HttpHandler respond(int responseCode, Supplier<String> bodySupplier) {
        return exchange -> {
            String responseBody = bodySupplier.get();
            byte[] responseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseCode, responseBodyBytes.length);
            try (OutputStream stream = exchange.getResponseBody()) {
                stream.write(responseBodyBytes);
            }
        };
    }

    /**
     * Create a context at the given path, with the given description and handler.
     *
     * @param description the description of the context.
     *                    This will be displayed in the root context.
     * @param path        the path of the context.
     *                    It must start with a slash, not end with a slash, and contain a colon, a double slash, or a question mark.
     *                    The path is treated as a URI path, so it is encoded by default.
     *                    For example, if the server is bound to localhost:8080, and the path is "/foo",
     *                    then the context will be accessible at <code>localhost:8080/foo</code>.
     * @param handler     the handler to use for the context.
     *                    The handler will be called for each request to the context.
     */
    public void createContext(String description, String path, HttpHandler handler) {
        logger.info("Created context at path %s".formatted(path));
        HttpContext context = server.createContext(path, handler);
        context.getAttributes().put("description", description);
        contexts.add(context);
    }

    /**
     * Start the server.
     */
    public void start() {
        logger.info("Server starting...");
        server.start();
        logger.info("Server started");
    }

    /**
     * Stop the server.
     *
     * @param timeout the timeout in milliseconds to wait for the server to die.
     */
    public void stop(int timeout) {
        logger.info("Server stopping in %ds...".formatted(timeout));
        server.stop(timeout);
        logger.info("Server stopped");
    }
}
