package com.skillerwhale.wordle.html;

public class GuessForm {

    public static String renderGuessForm() {
        return """
            <form method="post">
                <input type="text" name="latestGuess" placeholder="Enter your guess" required>
                <button type="submit">Submit Guess</button>
            </form>
        """;
    }
}
