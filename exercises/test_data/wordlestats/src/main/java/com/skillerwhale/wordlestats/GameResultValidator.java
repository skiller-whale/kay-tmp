package com.skillerwhale.wordlestats;

import java.util.regex.Pattern;

/**
 * Functions for working with GameResult objects.
 */
public class GameResultValidator {
    private static final Pattern WORD_PATTERN = Pattern.compile("^[a-z]{5}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    /**
     * Validates that a value is a well-formed GameResult.
     *
     * A valid GameResult must have:
     * - a non-empty string playerName
     * - a 5-letter lowercase alphabetic answer
     * - guesses as a non-empty array of 5-letter lowercase alphabetic strings
     * - date as a string matching YYYY-MM-DD
     * - the game must be in a completed state (won or lost, i.e. last guess
     *   matches answer or there are 6 guesses)
     *
     * Returns ValidationResult with valid=true or valid=false with a reason.
     */
    public static ValidationResult validateGameResult(GameResult result) {
        if (result == null) {
            return ValidationResult.failure("Result must be an object");
        }

        if (result.playerName() == null || result.playerName().isEmpty()) {
            return ValidationResult.failure("playerName must be a non-empty string");
        }

        if (!WORD_PATTERN.matcher(result.answer()).matches()) {
            return ValidationResult.failure("answer must be a 5-letter lowercase word");
        }

        if (result.guesses() == null || result.guesses().isEmpty()) {
            return ValidationResult.failure("guesses must be a non-empty array");
        }

        for (String guess : result.guesses()) {
            if (!WORD_PATTERN.matcher(guess).matches()) {
                return ValidationResult.failure("Each guess must be a 5-letter lowercase word");
            }
        }

        if (!DATE_PATTERN.matcher(result.date()).matches()) {
            return ValidationResult.failure("date must be a YYYY-MM-DD string");
        }

        String lastGuess = result.guesses().get(result.guesses().size() - 1);
        if (!lastGuess.equals(result.answer()) && result.guesses().size() < 6) {
            return ValidationResult.failure("Game must be in a completed state (won or lost)");
        }

        return ValidationResult.success();
    }

    /**
     * Returns a one-line summary of a game result.
     *
     * Format: "PlayerName: ANSWER n/6 ✓" (for wins)
     *     or: "PlayerName: ANSWER X/6 ✗" (for losses)
     *
     * The answer is displayed in uppercase.
     */
    public static String summarizeGame(GameResult result) {
        String answer = result.answer().toUpperCase();
        String lastGuess = result.guesses().get(result.guesses().size() - 1);
        boolean isWin = lastGuess.equals(result.answer());
        String status = isWin ? "✓" : "✗";
        String attempts = isWin ? String.valueOf(result.guesses().size()) : "X";
        return String.format("%s: %s %s/6 %s", result.playerName(), answer, attempts, status);
    }
}
