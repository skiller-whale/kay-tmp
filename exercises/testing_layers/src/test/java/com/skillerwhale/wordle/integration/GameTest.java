package com.skillerwhale.wordle.integration;

import com.microsoft.playwright.*;
import com.skillerwhale.wordle.helpers.Browser;
import com.skillerwhale.wordle.helpers.TestServer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;

import static com.skillerwhale.wordle.helpers.GameAssertions.*;
import static org.assertj.core.api.Assertions.*;

class GameTest {

    private static TestServer server;
    private static Playwright playwright;
    private static com.microsoft.playwright.Browser playwrightBrowser;
    private static BrowserContext context;

    private Browser browser;

    @BeforeAll
    static void setup() throws Exception {
        // Start test server on random port
        server = new TestServer();
        server.start();

        // Launch browser and create context once
        playwright = Playwright.create();
        playwrightBrowser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(true)
            .setArgs(java.util.Arrays.asList("--disable-dev-shm-usage", "--no-sandbox")));
        context = playwrightBrowser.newContext();
    }

    @AfterAll
    static void cleanup() {
        if (context != null) {
            context.close();
        }
        if (playwrightBrowser != null) {
            playwrightBrowser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        if (server != null) {
            server.close();
        }
    }

    @BeforeEach
    void createBrowser() {
        // Create new browser helper for each test
        Page page = context.newPage();
        browser = new Browser(page, server.getBaseUrl());
    }

    @AfterEach
    void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
    }

    @Test
    void shouldDisplayGameTitle() {
        browser.visit("/");

        assertThat(browser.getTitle()).isEqualTo("Skiller Wordle");
    }

    @Test
    void shouldDisplayGuessesAfterSubmission() {
        browser.visit("/?guesses=about,shark");

        assertGuessCount(browser, 2);
        assertGuessChar(browser, 0, 0, "A");
        assertGuessChar(browser, 0, 1, "B");
        assertGuessChar(browser, 0, 2, "O");
        assertGuessChar(browser, 0, 3, "U");
        assertGuessChar(browser, 0, 4, "T");

        assertGuessChar(browser, 1, 0, "S");
        assertGuessChar(browser, 1, 1, "H");
        assertGuessChar(browser, 1, 2, "A");
        assertGuessChar(browser, 1, 3, "R");
        assertGuessChar(browser, 1, 4, "K");
    }

    @Test
    void shouldShowWinMessageForCorrectGuess() {
        browser.visit("/?guesses=whale");

        assertGameWon(browser);
    }

    @Test
    void shouldShowLoseMessageAfterSixGuesses() {
        browser.visit("/?guesses=about,brain,cargo,dingo,elite,frame");

        assertGameLost(browser);
    }

    @Test
    void shouldPlayCompleteGame() {
        browser.visit("/");

        // Make several guesses
        browser.enterGuess("about");
        browser.enterGuess("brain");
        browser.enterGuess("cargo");

        // Should still be playing after 3 guesses
        assertGamePlaying(browser);

        // Make the winning guess
        browser.enterGuess("whale");

        // Should win the game
        assertGameWon(browser);
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldShowErrorMessageForInvalidGuesses() {
        // arrange
        browser.visit("/");

        // act

        // assert
        // Note: There is a helper method you can use to check error messages
        // assertErrorMessage(browser, expectedErrorMessage)
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldShowColourCodedFeedbackForPreviousGuesses() {
        // arrange
        browser.visit("/");

        // act
        // a useful guess to try here would be "water", which should be reported as:
        //   - green for the first character (W)
        //   - yellow for the second character (A)
        //   - gray for the third character (T)
        //   - yellow for the fourth character (E)
        //   - gray for the fifth character (R)


        // assert
        // NOTE: There is a helper method you can use to check square colours:
        // assertGuessCharColor(browser, guessIndex, charIndex, expectedColor)
        //
        // For example assertGuessCharColor(browser, 0, 2, "green") would check
        // that the third character of the first guess is green.
    }
}
