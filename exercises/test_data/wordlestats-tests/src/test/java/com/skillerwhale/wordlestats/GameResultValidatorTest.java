package com.skillerwhale.wordlestats;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameResultValidatorTest {

    @Test
    public void validateGameResult_AcceptsAValidGameResult() {
        GameResult result = new GameResult(
            "Alice",
            "whale",
            Arrays.asList("crane", "slate", "whale"),
            "2026-02-01"
        );

        ValidationResult validation = GameResultValidator.validateGameResult(result);

        assertTrue(validation.valid());
    }

    @Test
    public void validateGameResult_RejectsNull() {
        ValidationResult validation = GameResultValidator.validateGameResult(null);

        assertFalse(validation.valid());
        assertEquals("Result must be an object", validation.reason());
    }

    @Test
    public void validateGameResult_RejectsResultWithMissingPlayerName() {
        GameResult result = new GameResult(
            "",
            "whale",
            Arrays.asList("crane", "slate", "whale"),
            "2026-02-01"
        );

        ValidationResult validation = GameResultValidator.validateGameResult(result);

        assertFalse(validation.valid());
        assertEquals("playerName must be a non-empty string", validation.reason());
    }

    @Test
    public void validateGameResult_RejectsResultWithMissingAnswer() {
        GameResult result = new GameResult(
            "Alice",
            "",
            Arrays.asList("crane", "slate", "whale"),
            "2026-02-01"
        );

        ValidationResult validation = GameResultValidator.validateGameResult(result);

        assertFalse(validation.valid());
        assertEquals("answer must be a 5-letter lowercase word", validation.reason());
    }

    @ParameterizedTest
    @ValueSource(strings = {"whales", "WHALE", "wh4le", "whal", ""})
    public void validateGameResult_RejectsResultWithAnswerOfWrongFormat(String invalidAnswer) {
        GameResult result = new GameResult(
            "Alice",
            invalidAnswer,
            Arrays.asList("crane", "slate", "whale"),
            "2026-02-01"
        );

        ValidationResult validation = GameResultValidator.validateGameResult(result);

        assertFalse(validation.valid());
        assertEquals("answer must be a 5-letter lowercase word", validation.reason());
    }

    @Test
    public void validateGameResult_RejectsResultWithMissingGuesses() {
        GameResult result = new GameResult(
            "Alice",
            "whale",
            List.of(),
            "2026-02-01"
        );

        ValidationResult validation = GameResultValidator.validateGameResult(result);

        assertFalse(validation.valid());
        assertEquals("guesses must be a non-empty array", validation.reason());
    }

    @Test
    public void validateGameResult_RejectsResultWithInvalidDateFormat() {
        GameResult result = new GameResult(
            "Alice",
            "whale",
            Arrays.asList("crane", "slate", "whale"),
            "02-01-2026"
        );

        ValidationResult validation = GameResultValidator.validateGameResult(result);

        assertFalse(validation.valid());
        assertEquals("date must be a YYYY-MM-DD string", validation.reason());
    }

    @Test
    public void validateGameResult_RejectsResultThatIsNotInCompletedState() {
        GameResult result = new GameResult(
            "Alice",
            "whale",
            Arrays.asList("crane", "slate"),
            "2026-02-01"
        );

        ValidationResult validation = GameResultValidator.validateGameResult(result);

        assertFalse(validation.valid());
        assertEquals("Game must be in a completed state (won or lost)", validation.reason());
    }

    @ParameterizedTest
    @CsvSource({
        "Alice, whale, 'Alice: WHALE 1/6 ✓'",
        "Bob, flint, 'Bob: FLINT 3/6 ✓'",
        "Charlie, crane, 'Charlie: CRANE 6/6 ✓'"
    })
    public void summarizeGame_FormatsWinningGameCorrectly(String playerName, String answer, String expected) {
        List<String> guesses;
        if (answer.equals("whale")) {
            guesses = List.of("whale");
        } else if (answer.equals("flint")) {
            guesses = Arrays.asList("crane", "slate", "flint");
        } else { // crane
            guesses = Arrays.asList("stilt", "plumb", "vigor", "kayak", "monks", "crane");
        }

        GameResult result = new GameResult(playerName, answer, guesses, "2026-02-01");

        String summary = GameResultValidator.summarizeGame(result);

        assertEquals(expected, summary);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Alice", "Bob", "Charlie"})
    public void summarizeGame_FormatsLosingGameCorrectly(String playerName) {
        GameResult result = new GameResult(
            playerName,
            "whale",
            Arrays.asList("crane", "slate", "flame", "blame", "frame", "grape"),
            "2026-02-01"
        );

        String summary = GameResultValidator.summarizeGame(result);

        assertEquals(playerName + ": WHALE X/6 ✗", summary);
    }
}
