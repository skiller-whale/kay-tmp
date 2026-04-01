package com.skillerwhale.wordle;

import com.skillerwhale.wordle.helpers.Browser;
import com.skillerwhale.wordle.helpers.TestServer;
import com.skillerwhale.wordle.Database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;

import static com.skillerwhale.wordle.helpers.GameAssertions.*;
import static org.assertj.core.api.Assertions.*;

import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;

class GameTest {

    private static TestServer server;

    private Browser browser;

    @BeforeAll
    static void setup() throws Exception {
        // Start test server on random port
        Database database = new Database();
        server = new TestServer(database);
        server.start();
    }

    @AfterAll
    static void cleanup() {
        if (server != null) {
            server.close();
        }
    }

    @BeforeEach
    void createBrowser() {
        // Create new browser helper for each test
        browser = new Browser(server.getBaseUrl());
    }

    @AfterEach
    void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
    }

    @Test
    void shouldDisplayGameTitle() throws Exception {
        browser.visit("/");
        assertThat(browser.getTitle()).isEqualTo("Skiller Wordle");
    }

    @Test
    void shouldShowWinMessageForCorrectGuess() throws Exception {
        browser.visit("/");
        browser.clickNewGameButton();
        browser.enterGuess("whale");
        assertGameWon(browser);
    }

    @Test
    void shouldShowLoseMessageAndCorrectAnswerAfterSixGuesses() throws Exception {
        browser.visit("/");
        browser.clickNewGameButton();
        browser.enterGuess("about");
        browser.enterGuess("brain");
        browser.enterGuess("cargo");
        browser.enterGuess("dingo");
        browser.enterGuess("elite");
        browser.enterGuess("frame");
        assertGameLost(browser);

        assertCorrectAnswer(browser, "WHALE");
    }

    @Test
    void shouldShowErrorMessageForInvalidGuesses() throws Exception {
        browser.visit("/");
        browser.clickNewGameButton();
        browser.enterGuess("cat");
        assertErrorMessage(browser, "your guess is too short");
    }

    @Test
    void shouldShowColourCodedFeedbackForPreviousGuesses() throws Exception {
        // arrange
        browser.visit("/");
        browser.clickNewGameButton();

        // act
        browser.enterGuess("water");
        browser.enterGuess("shark");

        // assert
        assertGuessCharColor(browser, 0, 0, "green");
        assertGuessCharColor(browser, 0, 1, "yellow");
        assertGuessCharColor(browser, 0, 2, "gray");
        assertGuessCharColor(browser, 0, 3, "yellow");
        assertGuessCharColor(browser, 0, 4, "gray");

        assertGuessCharColor(browser, 1, 0, "gray");
        assertGuessCharColor(browser, 1, 1, "green");
        assertGuessCharColor(browser, 1, 2, "green");
        assertGuessCharColor(browser, 1, 3, "gray");
        assertGuessCharColor(browser, 1, 4, "gray");
    }
}
