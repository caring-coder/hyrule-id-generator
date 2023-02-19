package pro.verron.hyrule;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static pro.verron.hyrule.Server.respond;

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
        server.createContext("kill server", "/kill", respond(200, this::unlock));
        server.createContext("generate next id", "/hyrule/new-id", respond(200, this::newId));
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
