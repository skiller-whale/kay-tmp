package com.skillerwhale.wordlestats;

/**
 * Aggregate player statistics from arrays of GameResult objects.
 */
public record PlayerStatsData(
    int gamesPlayed,
    int gamesWon,
    double winRate,
    double averageAttempts
) {
}
