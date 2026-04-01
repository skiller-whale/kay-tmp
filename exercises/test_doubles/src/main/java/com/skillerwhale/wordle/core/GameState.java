package com.skillerwhale.wordle.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class GameState {
    private String id;
    private String correctAnswer;
    private List<String> guesses;
    private List<List<String>> evaluations;
    private String error;

    public GameState(String correctAnswer) {
        this(UUID.randomUUID().toString(), correctAnswer, null);
    }

    public GameState(String idString, String correctAnswer, String error) {
        this.id = idString;
        this.correctAnswer = correctAnswer;
        this.guesses = new ArrayList<>();
        this.evaluations = new ArrayList<>();
        this.error = error;
    }

    public void makeGuess(String guess) {
        String validationError = validateGuess(guess);
        if (validationError != null) {
            this.error = validationError;
            return;
        }

        this.error = null; // Clear any previous error
        this.guesses.add(guess);

        // Evaluate all guesses (to maintain consistency)
        this.evaluations.clear();
        for (String g : this.guesses) {
            this.evaluations.add(EvaluateGuess.evaluateGuess(this.correctAnswer, g));
        }
    }

    private static String validateGuess(String guess) {
        if (guess.length() < 5) {
            return "your guess is too short";
        } else if (guess.length() > 5) {
            return "your guess is too long";
        } else if (!Dictionary.contains(guess)) {
            return "Word not in dictionary";
        }
        return null;
    }

    public String getStatus() {
        if (guesses.isEmpty()) {
            return "playing";
        }

        String lastGuess = guesses.get(guesses.size() - 1);
        if (correctAnswer.equals(lastGuess)) {
            return "won";
        } else if (guesses.size() >= 6) {
            return "lost";
        } else {
            return "playing";
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getGuesses() {
        return new ArrayList<>(guesses);
    }

    public List<List<String>> getEvaluations() {
        return new ArrayList<>(evaluations);
    }

    public String getError() {
        return error;
    }
}
