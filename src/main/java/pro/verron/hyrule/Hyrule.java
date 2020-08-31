package pro.verron.hyrule;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.stream.Stream;

public class Hyrule {

    @SneakyThrows
    public static void main(String[] args) {
        int nbDigitsInIdRepresentation = 9;
        String prngStartingSeed = "Hyrule";
        int listeningPort = 8888;
        int serverDyingTimeout = 10;

        InetSocketAddress address = new InetSocketAddress(listeningPort);
        Iterator<Id> idIterator = Hyrule.idStream(nbDigitsInIdRepresentation, prngStartingSeed).iterator();

        Server server = new Server(idIterator);
        server.run(address, serverDyingTimeout);
    }

    @SneakyThrows
    public static Stream<Id> idStream(int nbChar, String initialSeed) {
        SecureRandom random = getSecureRandom(initialSeed);
        return new Generator(nbChar, random).asStream();
    }

    /**
     * Will create a SHA1PRNG random algorithm isntance, and seed it with the given String bytes.
     * Do not hesitate to give a relly long input String.
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
