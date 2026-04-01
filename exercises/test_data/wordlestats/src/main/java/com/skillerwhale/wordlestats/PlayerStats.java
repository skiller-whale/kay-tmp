package com.skillerwhale.wordlestats;

import java.util.List;

/**
 * Functions for calculating player statistics.
 */
public class PlayerStats {
    
    /**
     * Computes aggregate player statistics from arrays of GameResult objects.
     */
    public static PlayerStatsData calculatePlayerStats(List<GameResult> gameResults) {
        if (gameResults.isEmpty()) {
            return new PlayerStatsData(0, 0, 0.0, 0.0);
        }

        int gamesPlayed = gameResults.size();
        int gamesWon = (int) gameResults.stream()
            .filter(PlayerStats::isGameWon)
            .count();
        double winRate = (double) gamesWon / gamesPlayed;
        double averageAttempts = calculateAverageAttempts(gameResults);

        return new PlayerStatsData(gamesPlayed, gamesWon, winRate, averageAttempts);
    }

    /**
     * Calculates the average number of attempts for winning games only.
     * Returns 0 if there are no wins.
     */
    private static double calculateAverageAttempts(List<GameResult> gameResults) {
        List<GameResult> winningGames = gameResults.stream()
            .filter(PlayerStats::isGameWon)
            .toList();
        
        if (winningGames.isEmpty()) {
            return 0.0;
        }

        int totalAttempts = winningGames.stream()
            .mapToInt(r -> r.guesses().size())
            .sum();
        
        return (double) totalAttempts / winningGames.size();
    }

    /**
     * Determines whether a game is won or lost based on the guesses and answer.
     */
    private static boolean isGameWon(GameResult gameResult) {
        int lastIndex = gameResult.guesses().size() - 1;
        return gameResult.guesses().get(lastIndex).equals(gameResult.answer());
    }
}
