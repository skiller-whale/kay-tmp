package com.skillerwhale.wordle.core;

import java.util.List;
import java.util.ArrayList;

public class EvaluateGuess {

    public static List<String> evaluateGuess(String correctAnswer, String guess) {
        // Return a list of colors: "green", "yellow", "gray" for each letter
        // Green: correct letter in correct position
        // Yellow: correct letter in wrong position
        // Gray: letter not in the target word

        List<String> result = new ArrayList<>();
        List<Character> unmatchedLetters = new ArrayList<>();

        // Initialize result with "gray" for all positions
        for (int i = 0; i < guess.length(); i++) {
            result.add("gray");
        }

        // 1. First pass: mark exact matches (green)
        for (int i = 0; i < correctAnswer.length() && i < guess.length(); i++) {
            char correctLetter = correctAnswer.charAt(i);
            char guessLetter = guess.charAt(i);
            if (correctLetter == guessLetter) {
                result.set(i, "green");
            } else {
                unmatchedLetters.add(correctLetter);
            }
        }

        // 2. Second pass: mark partial matches (yellow)
        for (int i = 0; i < guess.length(); i++) {
            char guessLetter = guess.charAt(i);
            if (result.get(i).equals("gray") && unmatchedLetters.contains(guessLetter)) {
                unmatchedLetters.remove((Character) guessLetter);
                result.set(i, "yellow");
            }
        }

        return result;
    }

    // Overloaded method for backward compatibility (uses hardcoded "whale")
    public static List<String> evaluateGuess(String guess) {
        return evaluateGuess("whale", guess);
    }
}
