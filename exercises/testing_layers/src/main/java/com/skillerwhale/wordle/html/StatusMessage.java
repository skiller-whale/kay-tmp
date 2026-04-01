package com.skillerwhale.wordle.html;

public class StatusMessage {

    public static String renderStatusMessage(String status) {
        return switch (status) {
            case "won" -> "<p>Congratulations! You won!</p>";
            case "lost" -> "<p>Game over! The word was WHALE.</p>";
            case "playing" -> "<p>Keep guessing!</p>";
            default -> "<p>Unknown game status.</p>";
        };
    }
}
