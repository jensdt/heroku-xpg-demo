package be.xplore;

import io.undertow.Undertow;
import io.undertow.util.Headers;

public class HelloHeroku {

    private int defaultPort;

    public HelloHeroku() {
        this(8080);
    }

    public HelloHeroku(int defaultPort) {
        this.defaultPort = defaultPort;
    }

    public static void main(String[] args) {
        Undertow server = new HelloHeroku().createServer();
        server.start();

        System.out.println("Started server! Hit ^C to stop");
    }

    protected Undertow createServer() {
        return Undertow.builder()
                    .addHttpListener(listenerPort(), listenerHost())
                    .setHandler(exchange -> {
                        System.out.println("Incoming request for " + exchange.getRequestPath());
                        
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");

                        if ("/example".equals(exchange.getRequestPath())) {
                            exchange.getResponseSender().send("This example is nice");
                        } else {
                            exchange.getResponseSender().send("Hello World");
                        }

                    }).build();
    }

    protected String listenerHost() {
        return "0.0.0.0";
    }

    protected int listenerPort() {
        String envPort = System.getenv("PORT");

        if (envPort == null) {
            return defaultPort;
        } else {
            return Integer.parseInt(envPort);
        }
    }
}
