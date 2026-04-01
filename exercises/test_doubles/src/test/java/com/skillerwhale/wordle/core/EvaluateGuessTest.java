package com.skillerwhale.wordle.core;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.*;

class EvaluateGuessTest {

    @Test
    void shouldReturnAllGrayColors_WhenNoLettersMatch() {
        List<String> result = EvaluateGuess.evaluateGuess("xxxxx");
        assertThat(result).isEqualTo(Arrays.asList("gray", "gray", "gray", "gray", "gray"));
    }

    @Test
    void shouldReturnGreenColors_ForLettersInTheCorrectPlace() {
        List<String> result = EvaluateGuess.evaluateGuess("whole");
        assertThat(result).isEqualTo(Arrays.asList("green", "green", "gray", "green", "green"));
    }

    @Test
    void shouldReturnYellowColor_WhenCorrectLetterIsInWrongPosition() {
        List<String> result = EvaluateGuess.evaluateGuess("hello");
        assertThat(result).isEqualTo(Arrays.asList("yellow", "yellow", "gray", "green", "gray"));
    }
}
