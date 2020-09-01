package pro.verron.hyrule;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Hyrule {

    static {
        readLoggingConfiguration("logging.properties");
        logger= Logger.getLogger(Hyrule.class.getName());
    }

    @SneakyThrows
    private static void readLoggingConfiguration(String name) {
        LogManager logManager = LogManager.getLogManager();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
        logManager.readConfiguration(is);
    }

    private static final Logger logger;

    @SneakyThrows
    public static void main(String[] args) {
        // TODO: Make all those parameters as program input (args or properties file)
        // TODO: Maybe could allow to start as a command line program as an option
        int nbDigitsInIdRepresentation = 9;
        String prngStartingSeed = "Hyrule";
        int listeningPort = 8888;
        int serverDyingTimeout = 10;

        logger.info("Hyrule identifier production system (HIPS) is starting :");
        logger.info(()-> MessageFormat.format("HIPS will listen on port {0}", listeningPort));

        InetSocketAddress address = new InetSocketAddress(listeningPort);
        Iterator<Id> idIterator = Hyrule
                .idGenerator(nbDigitsInIdRepresentation, prngStartingSeed)
                .iterator();

        Server server = new Server(idIterator);
        server.run(address, serverDyingTimeout);
    }

    @SneakyThrows
    public static IdGenerator idGenerator(int nbChar, String initialSeed) {
        SecureRandom random = getSecureRandom(initialSeed);
        return new DefaultGenerator(nbChar, random);
    }

    /**
     * Will create a SHA1PRNG random algorithm isntance, and seed it with the given String bytes.
     * Do not hesitate to give a really long input String.
     * That algorithm has been chosen for its strength, and for the ability to be fully seeded, so allowing unit testing
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
