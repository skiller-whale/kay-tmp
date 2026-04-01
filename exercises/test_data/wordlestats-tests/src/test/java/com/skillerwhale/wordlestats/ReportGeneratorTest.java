package com.skillerwhale.wordlestats;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReportGeneratorTest {

    @Test
    public void generatePlayerReport_IncludesPlayersDisplayNameAsHeader() {
        PlayerReportData playerStats = new PlayerReportData(
            "123",
            "test@example.com",
            "TestPlayer",
            50,
            25,
            0.5,
            3.5,
            2,
            "2024-01-01",
            "2024-06-01",
            10,
            90,
            List.of()
        );

        String report = ReportGenerator.generatePlayerReport(playerStats);

        assertTrue(report.contains("=== TestPlayer ==="));
    }

    @Test
    public void generatePlayerReport_IncludesPlayersIdAndEmail() {
        PlayerReportData playerStats = new PlayerReportData(
            "123",
            "test@example.com",
            "TestPlayer",
            50,
            25,
            0.5,
            3.5,
            2,
            "2024-01-01",
            "2024-06-01",
            10,
            90,
            List.of()
        );

        String report = ReportGenerator.generatePlayerReport(playerStats);

        assertTrue(report.contains("ID: 123"));
        assertTrue(report.contains("Email: test@example.com"));
    }

    @Test
    public void generatePlayerReport_IncludesStatsSectionWithGamesPlayedAndWinRate() {
        PlayerReportData playerStats = new PlayerReportData(
            "123",
            "test@example.com",
            "TestPlayer",
            50,
            25,
            0.5,
            3.5,
            2,
            "2024-01-01",
            "2024-06-01",
            10,
            90,
            List.of()
        );

        String report = ReportGenerator.generatePlayerReport(playerStats);

        assertTrue(report.contains("--- Stats ---"));
        assertTrue(report.contains("Games Played: 50"));
        assertTrue(report.contains("Games Won: 25"));
        assertTrue(report.contains("Win Rate: 50.0%"));
    }

    @Test
    public void generatePlayerReport_IncludesPerformanceSectionWithAverageAttemptsAndFastestWin() {
        PlayerReportData playerStats = new PlayerReportData(
            "123",
            "test@example.com",
            "TestPlayer",
            50,
            25,
            0.5,
            3.5,
            2,
            "2024-01-01",
            "2024-06-01",
            10,
            90,
            List.of()
        );

        String report = ReportGenerator.generatePlayerReport(playerStats);

        assertTrue(report.contains("--- Performance ---"));
        assertTrue(report.contains("Average Attempts: 3.5"));
        assertTrue(report.contains("Fastest Win: 2 guess(es)"));
    }

    @Test
    public void generatePlayerReport_IncludesActivitySectionWithFirstAndLastPlayedDates() {
        PlayerReportData playerStats = new PlayerReportData(
            "123",
            "test@example.com",
            "TestPlayer",
            50,
            25,
            0.5,
            3.5,
            2,
            "2024-01-01",
            "2024-06-01",
            10,
            90,
            List.of()
        );

        String report = ReportGenerator.generatePlayerReport(playerStats);

        assertTrue(report.contains("--- Activity ---"));
        assertTrue(report.contains("First Played: 2024-01-01"));
        assertTrue(report.contains("Last Played: 2024-06-01"));
    }

    @Test
    public void generatePlayerReport_IncludesRankingSectionWithRankAndPercentile() {
        PlayerReportData playerStats = new PlayerReportData(
            "123",
            "test@example.com",
            "TestPlayer",
            50,
            25,
            0.5,
            3.5,
            2,
            "2024-01-01",
            "2024-06-01",
            10,
            90,
            List.of()
        );

        String report = ReportGenerator.generatePlayerReport(playerStats);

        assertTrue(report.contains("--- Ranking ---"));
        assertTrue(report.contains("Rank: #10"));
        assertTrue(report.contains("Percentile: Top 90%"));
    }

    @Test
    public void generatePlayerReport_IncludesAchievementsSection() {
        PlayerReportData playerStats = new PlayerReportData(
            "123",
            "test@example.com",
            "TestPlayer",
            50,
            25,
            0.5,
            3.5,
            2,
            "2024-01-01",
            "2024-06-01",
            10,
            90,
            List.of()
        );

        String report = ReportGenerator.generatePlayerReport(playerStats);

        assertTrue(report.contains("--- Achievements ---"));
        assertTrue(report.contains("No achievements yet."));
    }

    @Test
    public void generatePlayerReport_FormatsEachAchievementWithTrophyEmoji() {
        PlayerReportData playerStats = new PlayerReportData(
            "123",
            "test@example.com",
            "TestPlayer",
            50,
            25,
            0.5,
            3.5,
            2,
            "2024-01-01",
            "2024-06-01",
            10,
            90,
            Arrays.asList("First Win", "10-Game Streak")
        );

        String report = ReportGenerator.generatePlayerReport(playerStats);

        assertTrue(report.contains("🏆 First Win"));
        assertTrue(report.contains("🏆 10-Game Streak"));
    }
}
