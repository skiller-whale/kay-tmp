package com.skillerwhale.wordlestats;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Functions for generating player reports.
 */
public class ReportGenerator {

    /**
     * Generates a multi-section text report for a player profile.
     *
     * The report includes these sections:
     *   === PlayerName ===
     *   ID: playerId
     *   Email: email
     *
     *   --- Stats ---
     *   Games Played: N
     *   Games Won: N
     *   Win Rate: N.N%
     *
     *   --- Performance ---
     *   Average Attempts: N.N
     *   Fastest Win: N guess(es)
     *
     *   --- Activity ---
     *   First Played: YYYY-MM-DD
     *   Last Played: YYYY-MM-DD
     *
     *   --- Ranking ---
     *   Rank: #N
     *   Percentile: Top N%
     *
     *   --- Achievements ---
     *   🏆 Achievement 1
     *   🏆 Achievement 2
     */
    public static String generatePlayerReport(PlayerReportData data) {
        String firstPlayedDate = data.firstPlayed().length() >= 10 
            ? data.firstPlayed().substring(0, 10) 
            : data.firstPlayed();
        String lastPlayedDate = data.lastPlayed().length() >= 10 
            ? data.lastPlayed().substring(0, 10) 
            : data.lastPlayed();

        return String.format("""
=== %s ===
ID: %s
Email: %s

--- Stats ---
Games Played: %d
Games Won: %d
Win Rate: %.1f%%

--- Performance ---
Average Attempts: %.1f
Fastest Win: %d guess(es)

--- Activity ---
First Played: %s
Last Played: %s

--- Ranking ---
Rank: #%d
Percentile: Top %d%%

--- Achievements ---
%s""",
            data.playerName(),
            data.playerId(),
            data.email(),
            data.gamesPlayed(),
            data.gamesWon(),
            data.winRate() * 100,
            data.averageAttempts(),
            data.fastestWin(),
            firstPlayedDate,
            lastPlayedDate,
            data.rank(),
            data.percentile(),
            formatAchievements(data.achievements())
        ).trim();
    }

    /**
     * Formats an array of achievement names into a display string.
     *
     * Returns "No achievements yet." for an empty array, or each
     * achievement on its own line with a trophy emoji prefix.
     */
    private static String formatAchievements(List<String> achievements) {
        if (achievements.isEmpty()) {
            return "No achievements yet.";
        }

        return achievements.stream()
            .map(a -> "🏆 " + a)
            .collect(Collectors.joining("\n"));
    }
}
