package com.skillerwhale.wordlestats;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * GameResult represents a single completed Wordle game.
 */
public record GameResult(
    @JsonProperty("playerName")
    String playerName,
    
    @JsonProperty("answer")
    String answer,
    
    @JsonProperty("guesses")
    List<String> guesses,
    
    @JsonProperty("date")
    String date
) {
    public GameResult {
        // Compact constructor for validation/normalization if needed
        if (guesses == null) {
            guesses = new ArrayList<>();
        }
        if (playerName == null) {
            playerName = "";
        }
        if (answer == null) {
            answer = "";
        }
        if (date == null) {
            date = "";
        }
    }
}
