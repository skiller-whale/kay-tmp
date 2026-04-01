package com.skillerwhale.wordle.html;

import java.util.List;
import java.util.ArrayList;

public class GuessesFeedback {

    public static String renderGuessesFeedback(List<String> guesses, List<List<String>> evaluations) {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"guesses\">\n");

        for (int i = 0; i < 6; i++) {
            html.append("    <div class=\"guess\">\n");

            if (i < guesses.size()) {
                // Show actual guess
                String guess = guesses.get(i);

                List<String> colours = evaluations.get(i);

                for (int j = 0; j < guess.length(); j++) {
                    char letter = guess.charAt(j);
                    String color = colours.get(j);

                    html.append("        <span class=\"").append(color).append("\">")
                        .append(Character.toUpperCase(letter)).append("</span>\n");
                }
            } else {
                // Show empty placeholder row
                for (int j = 0; j < 5; j++) {
                    html.append("        <span class=\"empty\"></span>\n");
                }
            }

            html.append("    </div>\n");
        }

        html.append("</div>\n");
        return html.toString();
    }

    private static List<String> getDefaultColors(int length) {
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            colors.add("gray");
        }
        return colors;
    }
}
