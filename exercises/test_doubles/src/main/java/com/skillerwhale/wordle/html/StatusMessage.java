package com.skillerwhale.wordle.html;

public class StatusMessage {

    public static String renderStatusMessage(String status) {
        return switch (status) {
            case "won" -> "<p class=\"status\">Congratulations! You won!</p>";
            case "lost" -> "<p class=\"status\">Game over! The word was <span class=\"correct-answer\">WHALE</span>.</p>";
            case "playing" -> "<p class=\"status\">Keep guessing!</p>";
            default -> "<p class=\"status\">Unknown game status.</p>";
        };
    }
}
