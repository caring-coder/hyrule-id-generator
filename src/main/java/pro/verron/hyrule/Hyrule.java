package pro.verron.hyrule;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;
import picocli.CommandLine.ParseResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class is the entry point of the program.
 * It will parse the command line arguments, and start the server.
 * <p>
 * It will also configure the logging system.
 */
public interface Hyrule {
    Logger logger = Logger.getLogger(Hyrule.class.getName());
    int ERROR_CODE = 1;
    int SUCCESS_CODE = 0;

    private static void readLoggingConfiguration(String name) throws IOException {
        LogManager logManager = LogManager.getLogManager();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
        logManager.readConfiguration(is);
    }

    static void main(String[] args) {
        CommandLine commandLine = new CommandLine(commandSpec());
        commandLine.setExecutionStrategy(Hyrule::run);
        commandLine.execute(args);
    }

    private static CommandSpec commandSpec() {
        CommandSpec spec = CommandSpec.create();
        spec.mixinStandardHelpOptions(true);
        spec.addOption(OptionSpec.builder("-s", "--size")
                .type(int.class)
                .description("size of the identifiers")
                .build());
        spec.addOption(OptionSpec.builder("--seed")
                .type(String.class)
                .description("starting seed for the generator")
                .build());
        spec.addOption(OptionSpec.builder("-p", "--port")
                .type(int.class)
                .description("listening port for http server")
                .build());
        spec.addOption(OptionSpec.builder("--timeout")
                .type(int.class)
                .description("waiting timeout for http server")
                .build());
        return spec;
    }

    private static int run(ParseResult args) {
        // handle requests for help or version information
        Integer helpExitCode = CommandLine.executeHelpRequest(args);
        if (helpExitCode != null) return helpExitCode;

        try {
            readLoggingConfiguration("logging.properties");
            // TODO: Make all those parameters as program input (args or properties file)
            // TODO: Maybe could allow to start as a command line program as an option
            int nbDigitsInIdRepresentation = args.matchedOptionValue("--size", 9);
            String prngStartingSeed = args.matchedOptionValue("--seed", "Hyrule");
            int listeningPort = args.matchedOptionValue("--port", 8888);
            int serverDyingTimeout = args.matchedOptionValue("--timeout", 10);

            SecureRandom secureRandom = getSecureRandom(prngStartingSeed);
            Iterator<Id> idIterator = RandomIdIterator
                    .generator(nbDigitsInIdRepresentation, secureRandom)
                    .iterator();

            logger.info(MessageFormat.format("HIPS will listen on port {0}", listeningPort));
            InetSocketAddress address = new InetSocketAddress(listeningPort);
            Server server = new Server(address);

            HyruleServer hyruleServer = new HyruleServer(idIterator);
            hyruleServer.run(server);

            server.stop(serverDyingTimeout);
            return SUCCESS_CODE;
        } catch (Exception e) {
            String className = Hyrule.class.getName();
            String methodName = Thread.currentThread().getStackTrace()[0].getMethodName();
            LogManager.getLogManager().getLogger(className).throwing(className, methodName, e);
            return ERROR_CODE;
        }
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
    static SecureRandom getSecureRandom(String initialSeed) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(initialSeed.getBytes(StandardCharsets.UTF_8));
        return random;
    }
}
