package com.skillerwhale.wordle.helpers;

import static org.assertj.core.api.Assertions.*;

/**
 * Custom assertion helpers for game-specific testing patterns.
 * These provide more readable and domain-specific assertions.
 */
public class GameAssertions {

    /**
     * Assert that the browser shows a winning game state.
     * @param browser The browser instance to check
     */
    public static void assertGameWon(Browser browser) {
        assertThat(browser.getStatus())
            .as("Game should show win message")
            .contains("Congratulations");

        assertThat(browser.hasForm())
            .as("Game form should not be present when game is won")
            .isFalse();
    }

    /**
     * Assert that the browser shows a losing game state.
     * @param browser The browser instance to check
     */
    public static void assertGameLost(Browser browser) {
        assertThat(browser.getStatus())
            .as("Game should show lose message")
            .contains("Game over");

        assertThat(browser.hasForm())
            .as("Game form should not be present when game is lost")
            .isFalse();
    }

    /**
     * Assert that the browser shows a playing game state.
     * @param browser The browser instance to check
     */
    public static void assertGamePlaying(Browser browser) {
        assertThat(browser.getStatus())
            .as("Game should show playing message")
            .contains("Keep guessing");

        assertThat(browser.hasForm())
            .as("Game form should be present when game is playing")
            .isTrue();
    }

    /**
     * Assert that the browser shows a specific error message.
     * @param browser The browser instance to check
     * @param expectedError The expected error message
     */
    public static void assertErrorMessage(Browser browser, String expectedError) {
        assertThat(browser.getError())
            .as("Should show error message: " + expectedError)
            .isEqualTo(expectedError);
    }

    /**
     * Assert that the browser shows no error message.
     * @param browser The browser instance to check
     */
    public static void assertNoError(Browser browser) {
        assertThat(browser.getError())
            .as("Should not show any error message")
            .isNull();
    }

    /**
     * Assert that a specific guess character has the expected color class.
     * @param browser The browser instance to check
     * @param guessIndex The index of the guess (0-based)
     * @param charIndex The index of the character (0-based)
     * @param expectedColor The expected color ("green", "yellow", or "gray")
     */
    public static void assertGuessCharColor(Browser browser, int guessIndex, int charIndex, String expectedColor) {
        String actualClass = browser.getGuessCharClass(guessIndex, charIndex);
        assertThat(actualClass)
            .as("Character at guess %d, position %d should have color %s", guessIndex, charIndex, expectedColor)
            .isEqualTo(expectedColor);
    }

    /**
     * Assert that a specific guess character has the expected letter.
     * @param browser The browser instance to check
     * @param guessIndex The index of the guess (0-based)
     * @param charIndex The index of the character (0-based)
     * @param expectedLetter The expected letter
     */
    public static void assertGuessChar(Browser browser, int guessIndex, int charIndex, String expectedLetter) {
        String actualChar = browser.getGuessChar(guessIndex, charIndex);
        assertThat(actualChar.toUpperCase())
            .as("Character at guess %d, position %d should be %s", guessIndex, charIndex, expectedLetter.toUpperCase())
            .isEqualTo(expectedLetter.toUpperCase());
    }

    /**
     * Assert that the correct number of guesses are displayed.
     * @param browser The browser instance to check
     * @param expectedCount The expected number of guesses
     */
    public static void assertGuessCount(Browser browser, int expectedCount) {
        assertThat(browser.getGuessCount())
            .as("Should show %d guesses", expectedCount)
            .isEqualTo(expectedCount);
    }

    /**
     * Assert that the page contains specific text.
     * @param browser The browser instance to check
     * @param expectedText The text that should be present
     */
    public static void assertContainsText(Browser browser, String expectedText) {
        assertThat(browser.containsText(expectedText))
            .as("Page should contain text: " + expectedText)
            .isTrue();
    }
}
