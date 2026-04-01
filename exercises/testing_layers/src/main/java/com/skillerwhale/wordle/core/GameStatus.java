package com.skillerwhale.wordle.core;

import java.util.List;

public class GameStatus {

    public static String gameStatus(List<String> guesses) {
        if (guesses.isEmpty()) {
            return "playing";
        }

        String lastGuess = guesses.get(guesses.size() - 1);
        if ("whale".equals(lastGuess)) {
            return "won";
        } else if (guesses.size() >= 6) {
            return "lost";
        } else {
            return "playing";
        }
    }
}
