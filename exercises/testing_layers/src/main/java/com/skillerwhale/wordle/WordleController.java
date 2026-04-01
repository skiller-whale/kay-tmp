package com.skillerwhale.wordle;

import com.skillerwhale.wordle.core.GameStatus;
import com.skillerwhale.wordle.core.EvaluateGuess;
import com.skillerwhale.wordle.core.ValidateGuess;
import com.skillerwhale.wordle.html.*;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordleController {

    public void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("GET".equals(method)) {
            handleGetRequest(exchange);
        } else if ("POST".equals(method)) {
            handlePostRequest(exchange);
        } else {
            sendResponse(exchange, 405, "Method Not Allowed", "text/plain");
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String url = "http://localhost:" + exchange.getLocalAddress().getPort() + uri.toString();

        // Get base game state from the URL query parameters
        GameState state = GameState.fromUrl(url);
        List<String> guesses = state.getGuesses();
        String error = state.getError();

        // Generate derived game state
        String status = GameStatus.gameStatus(guesses);

        // TODO: get colour evaluations for each guess, and pass them on to the guessesFeedback function

        // Build HTML content
        // Hint: you can use content.add(ErrorMessage.renderErrorMessage(error))
        // to include an error message
        List<String> content = new ArrayList<>();

        // Add guesses feedback
        content.add(GuessesFeedback.renderGuessesFeedback(guesses));

        // Add guess form if still playing
        if ("playing".equals(status)) {
            content.add(GuessForm.renderGuessForm());
        }

        // Add status message
        content.add(StatusMessage.renderStatusMessage(status));

        String html = PageRenderer.renderPage(content);
        sendResponse(exchange, 200, html, "text/html");
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        // Read request body to get form data
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> formData = parseFormData(requestBody);
        String latestGuess = formData.get("latestGuess");

        // Get base game state from the URL query parameters
        URI uri = exchange.getRequestURI();
        String url = "http://localhost:" + exchange.getLocalAddress().getPort() + uri.toString();
        GameState state = GameState.fromUrl(url);
        List<String> guesses = state.getGuesses();


        // TODO: validate the latest guess, and return an error message if it is invalid
        // String error = ValidateGuess.validateGuess;

        // Redirect with the latest guess added to query parameters (and no error)
        List<String> newGuesses = new ArrayList<>(guesses);
        if (latestGuess != null) {
            newGuesses.add(latestGuess.toLowerCase());
        }
        GameState nextState = new GameState(newGuesses, null);
        sendRedirect(exchange, nextState.toUrl());
    }

    private Map<String, String> parseFormData(String formData) {
        Map<String, String> result = new HashMap<>();
        if (formData == null || formData.isEmpty()) {
            return result;
        }

        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = java.net.URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = java.net.URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                result.put(key, value);
            }
        }
        return result;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String body, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, body.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void sendRedirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
    }
}
