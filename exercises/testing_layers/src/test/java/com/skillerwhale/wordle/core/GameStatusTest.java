package com.skillerwhale.wordle.core;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import static org.assertj.core.api.Assertions.*;

class GameStatusTest {

    @Test
    void shouldReturnPlayingStatus_WhenGameHasNoGuesses() {
        String result = GameStatus.gameStatus(Collections.emptyList());
        assertThat(result).isEqualTo("playing");
    }

    @Test
    void shouldReturnPlayingStatus_WhenGameIsIncomplete() {
        List<String> guesses = Arrays.asList("about", "brain");
        String result = GameStatus.gameStatus(guesses);
        assertThat(result).isEqualTo("playing");
    }

    @Test
    void shouldReturnWonStatus_WhenLastGuessIsCorrect() {
        List<String> guesses = Arrays.asList("about", "brain", "whale");
        String result = GameStatus.gameStatus(guesses);
        assertThat(result).isEqualTo("won");
    }

    @Test
    void shouldReturnLostStatus_WhenSixIncorrectGuessesAreMade() {
        List<String> guesses = Arrays.asList("about", "brain", "cargo", "dingo", "elite", "frame");
        String result = GameStatus.gameStatus(guesses);
        assertThat(result).isEqualTo("lost");
    }
}
