package com.skillerwhale.wordle;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class GameState {
    private List<String> guesses;
    private String error;

    public GameState() {
        this.guesses = new ArrayList<>();
        this.error = null;
    }

    public GameState(List<String> guesses, String error) {
        this.guesses = guesses != null ? new ArrayList<>(guesses) : new ArrayList<>();
        this.error = error;
    }

    public List<String> getGuesses() {
        return new ArrayList<>(guesses);
    }

    public void setGuesses(List<String> guesses) {
        this.guesses = guesses != null ? new ArrayList<>(guesses) : new ArrayList<>();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static GameState fromUrl(String url) {
        GameState state = new GameState();

        if (url == null || !url.contains("?")) {
            return state;
        }

        String queryString = url.substring(url.indexOf("?") + 1);
        String[] params = queryString.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);

                if ("guesses".equals(key) && !value.isEmpty()) {
                    state.guesses = new ArrayList<>(Arrays.asList(value.split(",")));
                } else if ("error".equals(key)) {
                    state.error = value;
                }
            }
        }

        return state;
    }

    public String toUrl() {
        StringBuilder url = new StringBuilder("/?");

        if (!guesses.isEmpty()) {
            String guessesParam = String.join(",", guesses);
            url.append("guesses=").append(URLEncoder.encode(guessesParam, StandardCharsets.UTF_8));
        }

        if (error != null) {
            if (!guesses.isEmpty()) {
                url.append("&");
            }
            url.append("error=").append(URLEncoder.encode(error, StandardCharsets.UTF_8));
        }

        return url.toString();
    }
}
