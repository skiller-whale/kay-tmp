package com.skillerwhale.wordle;

import com.skillerwhale.wordle.helpers.Browser;
import com.skillerwhale.wordle.helpers.TestServer;
import com.skillerwhale.wordle.Database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;

import static org.assertj.core.api.Assertions.*;

import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;

class StatsTest {
    private static TestServer server;

    private Browser browser;

    @BeforeAll
    static void setup() throws Exception {
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

    private void winGame(Browser browser) throws Exception {
        browser.visit("/");
        browser.clickNewGameButton();
        browser.enterGuess("whale");
    }

    private void loseGame(Browser browser) throws Exception {
        browser.visit("/");
        browser.clickNewGameButton();
        browser.enterGuess("fishy");
        browser.enterGuess("shark");
        browser.enterGuess("shell");
        browser.enterGuess("trout");
        browser.enterGuess("salty");
        browser.enterGuess("ocean");
    }

    @Test
    @Disabled("TODO: Implement test")
    void showsStatsAtEndOfGame() throws Exception {
        // Arrange: win and lose some games
        winGame(browser);
        loseGame(browser);
        winGame(browser);
        loseGame(browser);

        // Act: get stats test
        // This returns the text of an element that uses the css class 'stats'
        String statsText = browser.getStats();

        // Assert: check stats content
    }
}
