package com.skillerwhale.wordle;

import com.skillerwhale.wordle.core.*;
import com.skillerwhale.wordle.html.*;
import com.skillerwhale.wordle.lib.Cookies;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;

public class WordleController {
    private final GameRepository gameRepository;
    private final AnswerGenerator answerGenerator;

    public WordleController(GameRepository gameRepository) {
        this.gameRepository = Objects.requireNonNull(gameRepository, "gameRepository cannot be null");
        this.answerGenerator = new AnswerGenerator();
    }

    // GET /games/:id - Game page
    private void handleGameGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String gameId = path.substring("/games/".length());

        // Get the game from the repository
        GameState game = gameRepository.getGame(gameId);
        if (game == null) {
            sendResponse(exchange, 404, "<p>Game not found</p>", "text/html");
            return;
        }

        List<String> content = buildGameHTML(game);

        // Add new game form if game is over
        if (!"playing".equals(game.getStatus())) {
            content.add(NewGameForm.renderNewGameForm());
        }

        // TODO: get the user's game history and show user stats if the game is over

        String html = PageRenderer.renderPage(content);
        sendResponse(exchange, 200, html, "text/html");
    }

    // POST /games/:id - Make a guess
    private void handleGamePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String gameId = path.substring("/games/".length());

        GameState game = gameRepository.getGame(gameId);
        if (game == null) {
            sendResponse(exchange, 404, "<p>Game not found</p>", "text/html");
            return;
        }
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> formData = parseFormData(requestBody);
        String latestGuess = formData.get("latestGuess");

        if (latestGuess != null) {
            latestGuess = latestGuess.toLowerCase();
        }
        game.makeGuess(latestGuess);
        gameRepository.saveGame(game);

        if (!"playing".equals(game.getStatus())) {
            // TODO: update the user's game history
        }

        sendRedirect(exchange, "/games/" + game.getId() + "?");
    }

    /* -------------------------------------------------------------------------
    // You can ignore everything below this line for the exercises
    // -----------------------------------------------------------------------*/

    // Main request handler
    public void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("GET".equals(method)) {
            if ("/".equals(path)) {
                handleHomeGet(exchange);
            } else if (path.startsWith("/games/")) {
                handleGameGet(exchange);
            } else {
                sendResponse(exchange, 404, "Not Found", "text/plain");
            }
        } else if ("POST".equals(method)) {
            if ("/".equals(path)) {
                handleHomePost(exchange);
            } else if (path.startsWith("/games/")) {
                handleGamePost(exchange);
            } else {
                sendResponse(exchange, 404, "Not Found", "text/plain");
            }
        } else {
            sendResponse(exchange, 405, "Method Not Allowed", "text/plain");
        }
    }

    // GET / - Home page with new game form
    private void handleHomeGet(HttpExchange exchange) throws IOException {
        // Return the HTML for the home page with a button to start a new game
        List<String> content = new ArrayList<>();
        content.add(NewGameForm.renderNewGameForm());

        String html = PageRenderer.renderPage(content);
        sendResponse(exchange, 200, html, "text/html");
    }

    // POST / - Create new game
    private void handleHomePost(HttpExchange exchange) throws IOException {
        String correctAnswer = "whale"; // To be replaced with a call to AnswerGenerator later
        GameState game = new GameState(correctAnswer);
        gameRepository.saveGame(game);
        sendRedirect(exchange, "/games/" + game.getId());
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

    private List<String> buildGameHTML(GameState game) {
        List<String> content = new ArrayList<>();

        // Add guesses feedback
        content.add(GuessesFeedback.renderGuessesFeedback(game.getGuesses(), game.getEvaluations()));

        // Add guess form if still playing
        if ("playing".equals(game.getStatus())) {
            if (game.getError() != null) {
                content.add(ErrorMessage.renderErrorMessage(game.getError()));
            }
            content.add(GuessForm.renderGuessForm(game.getId()));
        }

        // Add status message
        content.add(StatusMessage.renderStatusMessage(game.getStatus()));

        return content;
    }
}
