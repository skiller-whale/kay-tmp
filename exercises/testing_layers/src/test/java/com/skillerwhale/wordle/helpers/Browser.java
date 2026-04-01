package com.skillerwhale.wordle.helpers;

import com.microsoft.playwright.*;

/**
 * Browser helper class that provides domain-specific methods for testing the Wordle game.
 * This abstracts away Playwright complexity and provides a more readable test API.
 */
public class Browser implements AutoCloseable {

    private final Page page;
    private final String baseUrl;

    public Browser(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    /**
     * Navigate to a path relative to the base URL.
     * @param path The path to navigate to (e.g., "/", "/?guesses=whale")
     */
    public void visit(String path) {
        String url = path.startsWith("/") ? baseUrl + path : path;
        page.navigate(url);
    }

    /**
     * Enter a guess into the game form and submit it.
     * @param guess The word to guess
     */
    public void enterGuess(String guess) {
        Locator input = page.locator("input[name='latestGuess']");
        Locator submitButton = page.locator("button[type='submit']");

        input.clear();
        input.fill(guess);
        submitButton.click();

        // Wait for redirect after form submission
        page.waitForURL("**/?*");
    }

    /**
     * Get the current game status message.
     * @return The status text (e.g., "Keep guessing!", "Congratulations! You won!")
     */
    public String getStatus() {
        Locator statusElement = page.locator("p");
        if (statusElement.count() == 0) {
            throw new RuntimeException("Status element not found on page");
        }
        return statusElement.textContent().trim();
    }

    /**
     * Get any error message displayed on the page.
     * @return The error message text, or null if no error is displayed
     */
    public String getError() {
        Locator errorElement = page.locator(".error");
        if (errorElement.count() == 0) {
            return null;
        }
        return errorElement.textContent().trim();
    }

    /**
     * Check if the game form is present (indicates game is still playable).
     * @return true if the form is present, false otherwise
     */
    public boolean hasForm() {
        return page.locator("form").count() > 0;
    }

    /**
     * Get the title of the page.
     * @return The page title text
     */
    public String getTitle() {
        Locator titleElement = page.locator("h1");
        if (titleElement.count() == 0) {
            throw new RuntimeException("Title element not found on page");
        }
        return titleElement.textContent().trim();
    }

    /**
     * Get the CSS class of a specific character in a specific guess.
     * This is useful for testing color-coded feedback.
     * @param guessIndex The index of the guess (0-based)
     * @param charIndex The index of the character within the guess (0-based)
     * @return The CSS class of the character span element
     */
    public String getGuessCharClass(int guessIndex, int charIndex) {
        Locator guessElement = page.locator(".guess").nth(guessIndex);
        Locator charElement = guessElement.locator("span").nth(charIndex);

        if (charElement.count() == 0) {
            throw new RuntimeException("Character element not found at guess " + guessIndex + ", char " + charIndex);
        }

        String className = charElement.getAttribute("class");
        return className != null ? className.trim() : "";
    }

    /**
     * Get the text content of a specific character in a specific guess.
     * @param guessIndex The index of the guess (0-based)
     * @param charIndex The index of the character within the guess (0-based)
     * @return The character text
     */
    public String getGuessChar(int guessIndex, int charIndex) {
        Locator guessElement = page.locator(".guess").nth(guessIndex);
        Locator charElement = guessElement.locator("span").nth(charIndex);

        if (charElement.count() == 0) {
            throw new RuntimeException("Character element not found at guess " + guessIndex + ", char " + charIndex);
        }

        return charElement.textContent().trim();
    }

    /**
     * Get the number of actual guesses currently displayed on the page.
     * This counts only non-empty guess rows (those with letters, not placeholder rows).
     * @return The number of actual guess rows with letters
     */
    public int getGuessCount() {
        // Count guess rows that contain spans without the "empty" class
        // Empty rows have spans with class="empty", filled rows have spans with letters
        int count = 0;
        Locator guesses = page.locator(".guess");
        for (int i = 0; i < guesses.count(); i++) {
            Locator guessRow = guesses.nth(i);
            // Check if this row has any spans that are not empty
            Locator nonEmptySpans = guessRow.locator("span:not(.empty)");
            if (nonEmptySpans.count() > 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Check if the page contains specific text.
     * @param text The text to search for
     * @return true if the text is found, false otherwise
     */
    public boolean containsText(String text) {
        return page.locator("body").textContent().contains(text);
    }

    @Override
    public void close() {
        if (page != null) {
            page.close();
        }
    }
}
