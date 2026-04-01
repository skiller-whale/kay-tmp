package com.skillerwhale.wordle.helpers;

import com.skillerwhale.wordle.WordleApplication;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Helper class for managing test server lifecycle.
 * Automatically allocates free ports and manages server startup/shutdown.
 */
public class TestServer implements AutoCloseable {
    
    private int port;
    private String baseUrl;
    private boolean started = false;
    
    /**
     * Start the test server on a random available port.
     * @throws IOException If the server cannot be started
     */
    public void start() throws IOException {
        if (started) {
            throw new IllegalStateException("Server is already started");
        }
        
        // Find a free port
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }
        
        // Start the Wordle server
        WordleApplication.startServer(port);
        baseUrl = "http://localhost:" + port;
        started = true;
    }
    
    /**
     * Stop the test server if it's running.
     */
    public void stop() {
        if (started) {
            WordleApplication.stopServer();
            started = false;
        }
    }
    
    /**
     * Get the port the server is running on.
     * @return The server port
     * @throws IllegalStateException If the server is not started
     */
    public int getPort() {
        if (!started) {
            throw new IllegalStateException("Server is not started");
        }
        return port;
    }
    
    /**
     * Get the base URL for the server.
     * @return The base URL (e.g., "http://localhost:12345")
     * @throws IllegalStateException If the server is not started
     */
    public String getBaseUrl() {
        if (!started) {
            throw new IllegalStateException("Server is not started");
        }
        return baseUrl;
    }
    
    /**
     * Check if the server is currently started.
     * @return true if the server is running, false otherwise
     */
    public boolean isStarted() {
        return started;
    }
    
    @Override
    public void close() {
        stop();
    }
}