package pro.verron.hyrule;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

class HyruleServer {

    private static final Logger logger = Logger.getLogger(HyruleServer.class.getName());

    private final Iterator<Id> idIterator;
    private final CountDownLatch lock;

    public HyruleServer(Iterator<Id> idIterator) {
        this.idIterator = idIterator;
        this.lock = new CountDownLatch(1);
    }

    public void run(Server server) throws InterruptedException {
        logger.info("Hyrule identifier production system (HIPS) is starting :");
        server.createContext("/kill/", Server.respond(200, this::unlock));
        server.createContext("/hyrule/new-id/", Server.respond(200, this::newId));
        server.start();
        lock.await();
    }

    public String newId() {
        return idIterator.next().representation();
    }

    public String unlock() {
        lock.countDown();
        return "Unlock thread";
    }

}
