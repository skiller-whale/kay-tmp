package com.skillerwhale.wordle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.AfterAll;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

class WordleControllerTest {

    private static int port;
    private static HttpClient httpClient;

    @BeforeAll
    static void setup() throws IOException {
        // Find a free port
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }

        // Start the server
        WordleApplication.startServer(port);

        // Create HTTP client
        httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    @AfterAll
    static void cleanup() {
        WordleApplication.stopServer();
    }

    @Test
    void shouldReturnGamePageOnGet() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("Content-Type")).isPresent()
            .get().asString().contains("text/html");
        assertThat(response.body()).contains("Skiller Wordle");
    }

    @Test
    void shouldIncludeGuessFormWhenPlaying() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("<form");
        assertThat(response.body()).contains("latestGuess");
    }

    @Test
    void shouldShowWinMessageWhenGameWon() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/?guesses=whale"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Congratulations");
        assertThat(response.body()).doesNotContain("<form");
    }

    @Test
    void shouldShowLoseMessageAfterSixGuesses() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/?guesses=about,brain,cargo,dingo,elite,frame"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Game over");
        assertThat(response.body()).doesNotContain("<form");
    }

    @Test
    void shouldRedirectAfterPostingGuess() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/?guesses=shark,water"))
            .POST(HttpRequest.BodyPublishers.ofString("latestGuess=about"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(302);
        assertThat(response.headers().firstValue("Location")).isPresent()
            .get().asString().contains("guesses=shark%2Cwater%2Cabout");
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldRedirectWithGuessNotAddedToUrlAfterInvalidGuess() throws Exception {
        // TODO
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldRedirectWithErrorMessageAddedToUrlAfterInvalidGuess() throws Exception {
        // TODO
        // Note: The redirected url should include a query parameter like
        // `error=your+guess+is+too+long` if the guess is too long.
    }
}
