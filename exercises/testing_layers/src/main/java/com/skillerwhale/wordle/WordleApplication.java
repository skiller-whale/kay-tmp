package com.skillerwhale.wordle;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class WordleApplication {

    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        int port = getPortFromArgs(args);
        startServer(port);

        System.out.println("Wordle server started on port " + port);
        System.out.println("Visit http://localhost:" + port + " to play!");

        // Keep the server running
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.stop(0);
        }));
    }

    public static void startServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        WordleController controller = new WordleController();
        server.createContext("/", controller::handleRequest);

        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
    }

    public static void stopServer() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }

    public static int getServerPort() {
        return server != null ? server.getAddress().getPort() : -1;
    }

    private static int getPortFromArgs(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0] + ", using 3001");
            }
        }
        return 3001;
    }
}
