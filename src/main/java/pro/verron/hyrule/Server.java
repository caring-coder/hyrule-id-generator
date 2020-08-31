package pro.verron.hyrule;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private final Iterator<Id> idIterator;

    Server(Iterator<Id> idIterator){
        this.idIterator = idIterator;
    }

    public void run(InetSocketAddress address, int timeout) throws InterruptedException, IOException {
        HttpServer server = HttpServer.create(address, 0);
        logger.info(()-> MessageFormat.format("Created server at address {0}", server.getAddress()));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        server.setExecutor(executor);

        CountDownLatch latch = new CountDownLatch(1);

        String killingUri = "/hyrule/kill";
        logger.info(MessageFormat.format("Loading context {0} to kill server.", killingUri));
        server.createContext(killingUri, this.kill(latch, timeout));

        String newIdUri = "/hyrule/new-id";
        logger.info(MessageFormat.format("Loading context {0} to query new id.", newIdUri));
        server.createContext(newIdUri, this::newId);

        logger.info("Trying to start server");
        server.start();
        logger.info("Started server");
        logger.info(MessageFormat.format("To kill server, query context ''{0}'' until latch count is 0, latch count is now {1}", killingUri, latch.getCount()));
        latch.await();

        logger.info("Trying to terminate threadpool");
        executor.awaitTermination(timeout / 2, TimeUnit.SECONDS);
        executor.shutdown();

        logger.info("Trying to stop server");
        server.stop(timeout / 2);
        logger.info("Stopped server");
    }

    private HttpHandler kill(CountDownLatch latch, int timeout) {
        return (exchange) -> {
            respond(exchange, 200, String.format("Killing the server in %ds", timeout));
            logger.info(()-> MessageFormat.format("Called killing server context, latch count is now ''{0}''", latch.getCount()));
            latch.countDown();

        };
    }

    private void newId(HttpExchange exchange) throws IOException {
        String representation = idIterator.next().representation();
        respond(exchange, 200, representation);
        logger.info(()-> MessageFormat.format("Called newId server context, answered {0}", representation));
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
