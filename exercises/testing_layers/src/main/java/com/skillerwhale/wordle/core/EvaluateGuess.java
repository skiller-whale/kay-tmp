package com.skillerwhale.wordle.core;

import java.util.List;
import java.util.ArrayList;

public class EvaluateGuess {

    public static List<String> evaluateGuess(String guess) {
        // TODO: implement Wordle color evaluation logic
        // Return a list of colors: "green", "yellow", "gray" for each letter
        // Green: correct letter in correct position
        // Yellow: correct letter in wrong position
        // Gray: letter not in the target word

        String target = "whale";
        List<String> result = new ArrayList<>();

        // For now, return all gray as placeholder
        for (int i = 0; i < guess.length(); i++) {
            result.add("gray");
        }

        // Example approach outlined in comments below:

        // 1. First pass: mark exact matches (green)
        // TODO: e.g. loop through and set result to green if the letter is in the correct position

        // 2. Second pass: mark partial matches (yellow)
        // TODO: e.g. loop through again and set result to yellow if the letter
        // is in the target but not in the correct position

        return result;
    }
}
