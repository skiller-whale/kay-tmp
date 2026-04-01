package com.skillerwhale.wordle.html;

public class GuessForm {
    public static String renderGuessForm(String gameId) {
        return String.format("""
            <form method="post" action="/games/%s">
                <input type="text" name="latestGuess" placeholder="Enter your guess" required>
                <button type="submit" name="submit">Submit Guess</button>
            </form>
        """, gameId);
    }
}
