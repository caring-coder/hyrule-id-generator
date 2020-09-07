package pro.verron.hyrule;

import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class HyruleServer {

    private static final Logger logger = Logger.getLogger(Hyrule.class.getName());

    private final Iterator<Id> idIterator;
    private final int listeningPort;
    private final int serverDyingTimeout;
    private final CountDownLatch lock;

    public HyruleServer(Iterator<Id> idIterator, int listeningPort, int serverDyingTimeout) {
        this.idIterator = idIterator;
        this.listeningPort = listeningPort;
        this.serverDyingTimeout = serverDyingTimeout;
        this.lock = new CountDownLatch(1);
    }

    public void run() throws InterruptedException {
        logger.info("Hyrule identifier production system (HIPS) is starting :");
        logger.info(() -> MessageFormat.format("HIPS will listen on port {0}", listeningPort));

        InetSocketAddress address = new InetSocketAddress(listeningPort);

        Server server = new Server(address);
        server.createContext("/kill/", Server.respond(200, this::unlock));
        server.createContext("/hyrule/new-id/", Server.respond(200, this::newId));
        server.start();

        lock.await();
        server.stop(serverDyingTimeout);
    }

    public String newId() {
        return idIterator.next().representation();
    }

    public String unlock() {
        lock.countDown();
        return "Unlock thread";
    }

}
