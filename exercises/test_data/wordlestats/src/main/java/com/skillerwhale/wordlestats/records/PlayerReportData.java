package com.skillerwhale.wordlestats;

import java.util.ArrayList;
import java.util.List;

/**
 * Data for generating a player report.
 */
public record PlayerReportData(
    String playerId,
    String email,
    String playerName,
    int gamesPlayed,
    int gamesWon,
    double winRate,
    double averageAttempts,
    int fastestWin,
    String firstPlayed,
    String lastPlayed,
    int rank,
    int percentile,
    List<String> achievements
) {
    public PlayerReportData {
        // Compact constructor for normalization
        if (playerId == null) playerId = "";
        if (email == null) email = "";
        if (playerName == null) playerName = "";
        if (firstPlayed == null) firstPlayed = "";
        if (lastPlayed == null) lastPlayed = "";
        if (achievements == null) achievements = new ArrayList<>();
    }
}
