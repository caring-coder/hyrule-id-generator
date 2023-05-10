package pro.verron.hyrule;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static pro.verron.hyrule.Server.respond;

/**
 * A server that generates ids.
 */
class HyruleServer {
    private static final Logger logger = Logger.getLogger(HyruleServer.class.getName());
    private final Iterator<Id> idIterator;
    private final CountDownLatch lock;
    private final Server server;

    /**
     * Create a new Hyrule server with two endpoints:
     * <ul>
     *     <li>GET /kill : stop the server</li>
     *     <li>GET /hyrule/new-id : return the next id</li>
     * </ul>
     *
     * @param idIterator the iterator to use to generate the ids.
     * @param address    the address to bind the server to.
     */
    public HyruleServer(Iterator<Id> idIterator, InetSocketAddress address) throws IOException {
        this.server = newServer(address);
        this.idIterator = idIterator;
        this.lock = new CountDownLatch(1);
    }

    private Server newServer(InetSocketAddress address) throws IOException {
        var server = new Server(address);
        server.createContext("kill server", "/kill", respond(200, this::unlock));
        server.createContext("generate next id", "/hyrule/new-id", respond(200, this::newId));
        return server;
    }

    /**
     * Start the server and wait for unlock, then stop the server.
     *
     * @param serverDyingTimeout the timeout in milliseconds to wait for the server to die.
     */
    public void run(int serverDyingTimeout) throws InterruptedException {
        logger.info("Hyrule identifier production system (HIPS) is starting :");
        server.start();
        lock.await();
        server.stop(serverDyingTimeout);
    }

    /**
     * Return the next id.
     */
    public String newId() {
        return idIterator.next().representation();
    }

    /**
     * Unlock the server.
     */
    public String unlock() {
        lock.countDown();
        return "Unlock thread";
    }
}
