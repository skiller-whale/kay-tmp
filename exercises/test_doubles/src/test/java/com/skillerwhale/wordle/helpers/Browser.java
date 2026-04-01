package com.skillerwhale.wordle.helpers;

import java.io.IOException;

import org.htmlunit.ElementNotFoundException;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTextInput;

/**
 * Browser helper class that provides domain-specific methods for testing the Wordle game.
 * This abstracts away HtmlUnit complexity and provides a more readable test API.
 */
public class Browser implements AutoCloseable {

    private final WebClient webClient;
    private HtmlPage currentPage;
    private final String baseUrl;

    public Browser(String baseUrl) {
        this.webClient = new WebClient();
        this.baseUrl = baseUrl;
    }

    /**
     * Navigate to a path relative to the base URL.
     * @param path The path to navigate to (e.g., "/", "/?guesses=whale")
     */
    public void visit(String path) throws Exception {
        String url = path.startsWith("/") ? baseUrl + path : path;
        currentPage = webClient.getPage(url);
    }

    /**
     * Enter a guess into the game form and submit it.
     * @param guess The word to guess
     * @throws IOException
     * @throws ElementNotFoundException
     */
    public void enterGuess(String guess) throws ElementNotFoundException, IOException {
        HtmlTextInput latestGuessInput = currentPage.getElementByName("latestGuess");
        if (latestGuessInput == null) {
            throw new RuntimeException("Latest Guess input not found on page");
        }
        latestGuessInput.setAttribute("value", guess);

        HtmlButton submitButton = currentPage.getElementByName("submit");
        if (submitButton == null) {
            throw new RuntimeException("Submit button not found on page");
        }
        currentPage = submitButton.click();
    }

    public void clickNewGameButton() throws IOException {
        HtmlElement newGameButton = currentPage.querySelector("button[type='submit']");
        if (newGameButton == null) {
            throw new RuntimeException("New Game button not found on page");
        }
        currentPage = newGameButton.click();
    }

    /**
     * Get the current game status message.
     * @return The status text (e.g., "Keep guessing!", "Congratulations! You won!")
     */
    public String getStatus() {
        HtmlElement statusElement = currentPage.querySelector(".status");
        if (statusElement == null) {
            return null;
        }
        return statusElement.getTextContent().trim();
    }

    /**
     * Get any error message displayed on the page.
     * @return The error message text, or null if no error is displayed
     */
    public String getError() {
        HtmlElement errorElement = currentPage.querySelector(".error");
        if (errorElement == null) {
            return null;
        }
        return errorElement.getTextContent().trim();
    }

    /**
     * Check if the game form is present (indicates game is still playable).
     * @return true if the form is present, false otherwise
     */
    public boolean hasForm() {
        return currentPage.querySelector("form") != null;
    }

    /**
     * Get the title of the page.
     * @return The page title text
     */
    public String getTitle() {
        HtmlElement titleElement = currentPage.querySelector("h1");
        if (titleElement == null) {
            throw new RuntimeException("Title element not found on page");
        }
        return titleElement.getTextContent().trim();
    }

    /**
     * Get the correct answer displayed on the page (only shown when game is over).
     * @return The correct answer text
     */
    public String getCorrectAnswer() {
        HtmlElement correctAnswerElement = currentPage.querySelector(".correct-answer");
        if (correctAnswerElement == null) {
            throw new RuntimeException("Correct answer element not found on page");
        }
        return correctAnswerElement.getTextContent().trim();
    }

    /**
     * Get the CSS class of a specific character in a specific guess.
     * This is useful for testing color-coded feedback.
     * @param guessIndex The index of the guess (0-based)
     * @param charIndex The index of the character within the guess (0-based)
     * @return The CSS class of the character span element
     */
    public String getGuessCharClass(int guessIndex, int charIndex) {
        DomNode guessElement = currentPage.querySelectorAll(".guess").get(guessIndex);
        DomNode charElement = guessElement.querySelectorAll("span").get(charIndex);
        if (charElement == null) {
            throw new RuntimeException("Character element not found at guess " + guessIndex + ", char " + charIndex);
        }
        return charElement.getAttributes().getNamedItem("class").getNodeValue();
    }

    /**
     * Get the text content of a specific character in a specific guess.
     * @param guessIndex The index of the guess (0-based)
     * @param charIndex The index of the character within the guess (0-based)
     * @return The character text
     */
    public String getGuessChar(int guessIndex, int charIndex) {
        DomNode guessElement = currentPage.querySelectorAll(".guess").get(guessIndex);
        DomNode charElement = guessElement.querySelectorAll("span").get(charIndex);
        if (charElement == null) {
            throw new RuntimeException("Character element not found at guess " + guessIndex + ", char " + charIndex);
        }
        return charElement.getTextContent().trim();
    }

    /**
     * Get the game statistics text from the stats section.
     * @return The stats text (e.g., "Games won: 2, Games lost: 3, Total played: 5")
     */
    public String getStats() {
        DomNode statsElement = currentPage.querySelector(".stats");
        if (statsElement == null) {
            throw new RuntimeException("Stats element not found on page");
        }
        return statsElement.getTextContent().trim();
    }

    /**
     * Get the number of actual guesses currently displayed on the page.
     * This counts only non-empty guess rows (those with letters, not placeholder rows).
     * @return The number of actual guess rows with letters
     */
    public int getGuessCount() {
        // Count guess rows that contain spans without the "empty" class
        // Empty rows have spans with class="empty", filled rows have spans with letters
        var countGuessRows = currentPage.querySelectorAll(".guess").size();
        var countEmptyRows = currentPage.querySelectorAll(".empty").size();
        return countGuessRows - countEmptyRows;
    }

    /**
     * Check if the page contains specific text.
     * @param text The text to search for
     * @return true if the text is found, false otherwise
     */
    public boolean containsText(String text) {
        return currentPage.querySelector("body").getTextContent().contains(text);
    }

    @Override
    public void close() {
        if (currentPage != null) {
            currentPage = null;
        }
    }
}
