package com.skillerwhale.wordlestats;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerStatsTest {

    @Test
    public void calculatePlayerStats_Returns0sForEmptyArray() {
        PlayerStatsData stats = PlayerStats.calculatePlayerStats(List.of());

        assertEquals(0, stats.gamesPlayed());
        assertEquals(0, stats.gamesWon());
    }

    @Test
    public void calculatePlayerStats_CountsGamesPlayedAndWonCorrectly() {
        List<GameResult> games = Arrays.asList(
            new GameResult("Alice", "whale", List.of("whale"), "2026-02-01"),
            new GameResult("Alice", "crane", 
                Arrays.asList("stilt", "plumb", "vigor", "kayak", "monks", "brand"), 
                "2026-02-02")
        );

        PlayerStatsData stats = PlayerStats.calculatePlayerStats(games);

        assertEquals(2, stats.gamesPlayed());
        assertEquals(1, stats.gamesWon());
    }

    @Test
    public void calculatePlayerStats_Returns1WhenAllGamesAreWon() {
        List<GameResult> games = Arrays.asList(
            new GameResult("Alice", "whale", List.of("whale"), "2026-02-01"),
            new GameResult("Alice", "crane", List.of("crane"), "2026-02-02")
        );

        PlayerStatsData stats = PlayerStats.calculatePlayerStats(games);

        assertEquals(1.0, stats.winRate());
    }

    @Test
    public void calculatePlayerStats_ReturnsCorrectRatioForMixOfWinsAndLosses() {
        List<GameResult> games = Arrays.asList(
            new GameResult("Alice", "whale", List.of("whale"), "2026-02-01"),
            new GameResult("Alice", "crane", 
                Arrays.asList("stilt", "plumb", "vigor", "kayak", "monks", "brand"), 
                "2026-02-02"),
            new GameResult("Alice", "flint", List.of("flint"), "2026-02-03")
        );

        PlayerStatsData stats = PlayerStats.calculatePlayerStats(games);

        assertEquals(2.0 / 3.0, stats.winRate(), 0.00001);
    }

    @Test
    public void calculatePlayerStats_Returns0AverageAttemptsWhenThereAreNoWins() {
        List<GameResult> games = List.of(
            new GameResult("Alice", "whale", 
                Arrays.asList("stilt", "plumb", "vigor", "kayak", "monks", "brand"), 
                "2026-02-01")
        );

        PlayerStatsData stats = PlayerStats.calculatePlayerStats(games);

        assertEquals(0.0, stats.averageAttempts());
    }

    @Test
    public void calculatePlayerStats_CalculatesMeanAttemptsForWinningGames() {
        List<GameResult> games = Arrays.asList(
            new GameResult("Alice", "whale", 
                Arrays.asList("crane", "slate", "whale"), 
                "2026-02-01"),
            new GameResult("Alice", "crane", 
                Arrays.asList("stilt", "plumb", "vigor", "kayak", "monks", "brand"), 
                "2026-02-02"),
            new GameResult("Alice", "flint", List.of("flint"), "2026-02-03")
        );

        PlayerStatsData stats = PlayerStats.calculatePlayerStats(games);

        assertEquals((3 + 1) / 2.0, stats.averageAttempts());
    }

    @Test
    public void calculatePlayerStats_IgnoresLostGamesWhenCalculatingAverage() {
        List<GameResult> games = List.of(
            new GameResult("Alice", "whale", 
                Arrays.asList("stilt", "plumb", "vigor", "kayak", "monks", "brand"), 
                "2026-02-01")
        );

        PlayerStatsData stats = PlayerStats.calculatePlayerStats(games);

        assertEquals(0.0, stats.averageAttempts());
    }
}
