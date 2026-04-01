package com.skillerwhale.wordle.html;

import java.util.List;
import java.util.ArrayList;

public class GuessesFeedback {

    public static String renderGuessesFeedback(List<String> guesses) {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"guesses\">\n");

        for (int i = 0; i < 6; i++) {
            if (i < guesses.size()) {
                html.append(renderGuessFeedback(guesses.get(i)));
            } else {
                // Show empty placeholder row
                html.append(renderEmptyGuess());
            }
        }

        html.append("</div>\n");
        return html.toString();
    }

    public static String renderEmptyGuess() {
        return "<div class=\"guess\">\n" +
               "    <span class=\"empty\"></span>\n" +
               "    <span class=\"empty\"></span>\n" +
               "    <span class=\"empty\"></span>\n" +
               "    <span class=\"empty\"></span>\n" +
               "    <span class=\"empty\"></span>\n" +
               "</div>\n";
    }

    public static String renderGuessFeedback(String guess) {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"guess\">\n");

        for (int j = 0; j < guess.length(); j++) {
            char letter = guess.charAt(j);
            html.append(String.format(
                "<span class=\"%s\">%s</span>\n",
                "grey",
                Character.toUpperCase(letter))
            );
        }

        html.append("</div>\n");
        return html.toString();
    }
}
