/**
 * This module is the main module of the project. It contains the main class and the module descriptor.
 * The main class is {@link pro.verron.hyrule.Hyrule}, which is the entry point of the program.
 *
 * @author Joseph Verron
 */
module pro.verron.hyrule {
    exports pro.verron.hyrule;

    opens pro.verron.hyrule;

    requires jdk.httpserver;
    requires java.net.http;
    requires java.logging;
    requires info.picocli;
}