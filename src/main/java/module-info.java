module pro.verron.hyrule {
    exports pro.verron.hyrule;

    opens pro.verron.hyrule;

    requires jdk.httpserver;
    requires java.net.http;
    requires java.logging;
    requires info.picocli;
}