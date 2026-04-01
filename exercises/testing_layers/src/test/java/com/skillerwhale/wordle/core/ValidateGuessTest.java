package com.skillerwhale.wordle.core;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ValidateGuessTest {

    @Test
    void shouldReturnNull_WhenGuessIsValid() {
        String result = ValidateGuess.validateGuess("whale");
        assertThat(result).isNull();
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldReturnError_WhenGuessIsTooShort() {
        // TODO
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldReturnError_WhenGuessIsTooLong() {
        // TODO
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldReturnError_WhenWordIsNotInDictionary() {
        // TODO
    }
}
