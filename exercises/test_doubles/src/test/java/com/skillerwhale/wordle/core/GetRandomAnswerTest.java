package com.skillerwhale.wordle.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
// NOTE: When implementing tests that use Mockito, add these imports:
// import org.mockito.MockedStatic;
// import org.mockito.Mockito;
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.Mockito.*;

public class GetRandomAnswerTest {

    @Test
    @Disabled("TODO: implement test")
    void shouldReturnAFiveLetterWord() {
        // TODO: Test that getRandomAnswer returns a 5-letter word
        // Hint: You'll need to implement the method first to make this pass
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldReturnAWordFromTheDictionary() {
        // TODO: Test that getRandomAnswer returns a word that exists in the Dictionary
        // Hint: Use Dictionary.contains() to verify the returned word is valid
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldReturnDifferentWordsOnMultipleCalls() {
        // TODO: Test that getRandomAnswer can return different words
        // Call it multiple times and check that not all results are the same
        // Note: This test might occasionally fail due to randomness - that's expected
    }

    @Test
    @Disabled("TODO: implement test")
    void shouldUseRandomNumberGeneratorToSelectWord() {
        // TODO: Test that getRandomAnswer uses a random number generator
        // Use Mockito to mock the random number generation and verify it's called
        // This test should drive you to inject the randomness as a dependency
    }
}
