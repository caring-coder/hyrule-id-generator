package pro.verron.hyrule;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Hyrule {

    private static final Logger logger;

    static {
        readLoggingConfiguration("logging.properties");
        logger = Logger.getLogger(Hyrule.class.getName());
    }

    @SneakyThrows
    private static void readLoggingConfiguration(String name) {
        LogManager logManager = LogManager.getLogManager();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
        logManager.readConfiguration(is);
    }

    @SneakyThrows
    public static void main(String[] args) {
        // TODO: Make all those parameters as program input (args or properties file)
        // TODO: Maybe could allow to start as a command line program as an option
        int nbDigitsInIdRepresentation = 9;
        String prngStartingSeed = "Hyrule";
        int listeningPort = 8888;
        int serverDyingTimeout = 10;

        logger.info("Hyrule identifier production system (HIPS) is starting :");
        logger.info(() -> MessageFormat.format("HIPS will listen on port {0}", listeningPort));

        InetSocketAddress address = new InetSocketAddress(listeningPort);
        Iterator<Id> idIterator = Hyrule
                .idGenerator(nbDigitsInIdRepresentation, prngStartingSeed)
                .iterator();


        CountDownLatch lock = new CountDownLatch(1);

        Server server = new Server(address);
        server.createContext("/kill/", Server.respond(200, () -> unlock(lock)));
        server.createContext("/hyrule/new-id/", Server.respond(200, () -> idIterator.next().representation()));
        server.start();

        lock.await();

        server.stop(serverDyingTimeout);
    }

    public static String unlock(CountDownLatch lock) {
        lock.countDown();
        return "Unlock thread";
    }

    public static IdGenerator idGenerator(int nbChar, String initialSeed) throws NoSuchAlgorithmException {
        SecureRandom random = getSecureRandom(initialSeed);
        assert nbChar > 0 : "Only positive upper bound is being considered";
        int upperBound = computeHighestPossibleValue(nbChar);
        RandomIdIterator iterator = new RandomIdIterator(nbChar, random, upperBound);
        return new DistinctGenerator(iterator);
    }

    private static int computeHighestPossibleValue(int nbChar) {
        int top = 0;
        for(int i = 0; i < nbChar; i++){
            top = top * 10 + 9;
        }
        return top;
    }

    /**
     * Will create a SHA1PRNG random algorithm instance, and seed it with the given String bytes.
     * Do not hesitate to give a really long input String.
     * That algorithm has been chosen for its strength, and for the ability to be fully seeded, so allowing unit testing
     *
     * @param initialSeed will be used to seed the SecureRandom instance
     * @return a seeded SecureRandom instance
     * @throws NoSuchAlgorithmException in case there is no provider for SHA1PRNG algorithm
     */
    private static SecureRandom getSecureRandom(String initialSeed) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(initialSeed.getBytes(StandardCharsets.UTF_8));
        return random;
    }

}
