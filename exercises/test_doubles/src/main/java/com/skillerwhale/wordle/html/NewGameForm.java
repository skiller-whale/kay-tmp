package com.skillerwhale.wordle.html;

public class NewGameForm {
    
    public static String renderNewGameForm() {
        return """
            <form method="post" action="/">
                <button type="submit">Start New Game</button>
            </form>
            """;
    }
}