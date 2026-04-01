package com.skillerwhale.wordle.core;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class GameStateTest {

    @Test
    void shouldReturnPlayingStatus_WhenGameHasNoGuesses() {
        GameState gameState = new GameState("whale");
        String result = gameState.getStatus();
        assertThat(result).isEqualTo("playing");
    }

    @Test
    void shouldReturnPlayingStatus_WhenGameIsIncomplete() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("about");
        gameState.makeGuess("brain");

        String result = gameState.getStatus();
        assertThat(result).isEqualTo("playing");
    }

    @Test
    void shouldReturnWonStatus_WhenLastGuessIsCorrect() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("about");
        gameState.makeGuess("brain");
        gameState.makeGuess("whale");

        String result = gameState.getStatus();
        assertThat(result).isEqualTo("won");
    }

    @Test
    void shouldReturnLostStatus_WhenSixIncorrectGuessesAreMade() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("about");
        gameState.makeGuess("brain");
        gameState.makeGuess("cargo");
        gameState.makeGuess("dingo");
        gameState.makeGuess("elite");
        gameState.makeGuess("frame");

        String result = gameState.getStatus();
        assertThat(result).isEqualTo("lost");
    }

    @Test
    void shouldStoreGuessesCorrectly() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("about");
        gameState.makeGuess("brain");

        List<String> guesses = gameState.getGuesses();
        assertThat(guesses).containsExactly("about", "brain");
    }

    @Test
    void shouldGenerateUniqueId() {
        GameState gameState1 = new GameState("whale");
        GameState gameState2 = new GameState("whale");

        assertThat(gameState1.getId()).isNotEqualTo(gameState2.getId());
        assertThat(gameState1.getId()).isNotNull();
        assertThat(gameState2.getId()).isNotNull();
    }

    @Test
    void shouldStoreCorrectAnswer() {
        GameState gameState = new GameState("whale");
        assertThat(gameState.getCorrectAnswer()).isEqualTo("whale");
    }

    @Test
    void shouldSetErrorOnInvalidGuess() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("abc"); // too short

        assertThat(gameState.getError()).isNotNull();
        assertThat(gameState.getGuesses()).isEmpty(); // guess should not be added
        assertThat(gameState.getStatus()).isEqualTo("playing");
    }

    @Test
    void shouldClearErrorOnValidGuess() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("abc"); // invalid guess
        assertThat(gameState.getError()).isNotNull();

        gameState.makeGuess("about"); // valid guess
        assertThat(gameState.getError()).isNull();
        assertThat(gameState.getGuesses()).containsExactly("about");
    }

    @Test
    void shouldGenerateEvaluationsForGuesses() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("about");
        gameState.makeGuess("whale");

        List<List<String>> evaluations = gameState.getEvaluations();
        assertThat(evaluations).hasSize(2);
        assertThat(evaluations.get(0)).hasSize(5); // evaluation for "about"
        assertThat(evaluations.get(1)).hasSize(5); // evaluation for "whale"
    }

    @Test
    void shouldAcceptValidGuess() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("whale");

        assertThat(gameState.getError()).isNull();
        assertThat(gameState.getGuesses()).containsExactly("whale");
        assertThat(gameState.getStatus()).isEqualTo("won");
    }

    @Test
    void shouldRejectGuessWhenTooShort() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("cat");

        assertThat(gameState.getError()).isEqualTo("your guess is too short");
        assertThat(gameState.getGuesses()).isEmpty();
        assertThat(gameState.getStatus()).isEqualTo("playing");
    }

    @Test
    void shouldRejectGuessWhenTooLong() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("crocodile");

        assertThat(gameState.getError()).isEqualTo("your guess is too long");
        assertThat(gameState.getGuesses()).isEmpty();
    }

    @Test
    void shouldRejectGuessWhenWordNotInDictionary() {
        GameState gameState = new GameState("whale");
        gameState.makeGuess("zzzzz");

        assertThat(gameState.getError()).isEqualTo("Word not in dictionary");
        assertThat(gameState.getGuesses()).isEmpty();
    }

    @Test
    void shouldClearErrorsAfterValidGuess() {
        GameState gameState = new GameState("whale");

        gameState.makeGuess("cat");
        assertThat(gameState.getError()).isEqualTo("your guess is too short");
        assertThat(gameState.getGuesses()).isEmpty();

        // Valid guess should clear error and add to guesses
        gameState.makeGuess("about");
        assertThat(gameState.getError()).isNull();
        assertThat(gameState.getGuesses()).containsExactly("about");
    }

}
