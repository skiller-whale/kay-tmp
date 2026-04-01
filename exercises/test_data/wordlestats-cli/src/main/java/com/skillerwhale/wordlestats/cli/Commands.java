package com.skillerwhale.wordlestats.cli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillerwhale.wordlestats.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Commands {
    private static List<GameResult> gameResults;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static List<GameResult> getGameResults() {
        if (gameResults == null) {
            try {
                // Look for data in /app/data (Docker) or relative to current directory (local)
                File containerDataFile = new File("/app/data/sample-results.json");
                File localDataFile = new File("data/sample-results.json");
                File dataFile = containerDataFile.exists() ? containerDataFile : localDataFile;

                gameResults = objectMapper.readValue(dataFile, new TypeReference<List<GameResult>>() {});
            } catch (IOException e) {
                System.err.println("Error reading data file: " + e.getMessage());
                gameResults = new ArrayList<>();
            }
        }
        return gameResults;
    }

    public static List<String> getPlayerNames() {
        return getGameResults().stream()
            .map(GameResult::playerName)
            .distinct()
            .collect(Collectors.toList());
    }

    public static List<GameResult> getPlayerGames(String playerName) {
        return getGameResults().stream()
            .filter(r -> r.playerName().equals(playerName))
            .collect(Collectors.toList());
    }

    public static void listGames() {
        System.out.println("\n=== All Game Results ===\n");
        List<GameResult> results = getGameResults();
        int maxIndexWidth = String.valueOf(results.size()).length();
        
        for (int i = 0; i < results.size(); i++) {
            GameResult result = results.get(i);
            String index = String.format("%" + maxIndexWidth + "d", i + 1);
            System.out.printf("%s. %s [%s]%n", index, GameResultValidator.summarizeGame(result), result.date());
        }
        System.out.printf("%nTotal: %d games%n%n", results.size());
    }

    public static void showPlayerStats(String playerName) {
        List<GameResult> playerGames = getPlayerGames(playerName);
        if (playerGames.isEmpty()) {
            System.out.printf("%nNo games found for player: %s%n%n", playerName);
            return;
        }

        PlayerStatsData stats = PlayerStats.calculatePlayerStats(playerGames);
        System.out.printf("%n=== Stats for %s ===%n%n", playerName);
        System.out.printf("Games Played: %d%n", stats.gamesPlayed());
        System.out.printf("Games Won: %d%n", stats.gamesWon());
        System.out.printf("Win Rate: %.1f%%%n", stats.winRate() * 100);
        System.out.printf("Average Attempts: %.1f%n%n", stats.averageAttempts());
    }

    public static void showPlayerReport(String playerName) {
        List<GameResult> playerGames = getPlayerGames(playerName);
        if (playerGames.isEmpty()) {
            System.out.printf("%nNo games found for player: %s%n%n", playerName);
            return;
        }

        PlayerStatsData stats = PlayerStats.calculatePlayerStats(playerGames);

        // Calculate rankings
        List<PlayerRank> allPlayerStats = getPlayerNames().stream()
            .map(name -> new PlayerRank(name, PlayerStats.calculatePlayerStats(getPlayerGames(name))))
            .toList();
        
        List<RankedPlayer> ranked = rankPlayers(allPlayerStats);
        RankedPlayer playerRank = ranked.stream()
            .filter(p -> p.playerName.equals(playerName))
            .findFirst()
            .orElseThrow();

        // Calculate additional stats
        List<GameResult> wins = playerGames.stream()
            .filter(g -> g.guesses().get(g.guesses().size() - 1).equals(g.answer()))
            .toList();
        
        int fastestWin = wins.isEmpty() ? 0 : wins.stream()
            .mapToInt(g -> g.guesses().size())
            .min()
            .orElse(0);
        
        List<String> dates = playerGames.stream()
            .map(GameResult::date)
            .sorted()
            .toList();
        
        int percentile = (int) Math.round((double) playerRank.rank / ranked.size() * 100);

        // Generate sample achievements
        List<String> achievements = new ArrayList<>();
        if (stats.winRate() == 1.0) achievements.add("Perfect Record");
        if (fastestWin == 1) achievements.add("Hole-in-One");
        if (stats.gamesPlayed() >= 5) achievements.add("Dedicated Player");
        if (stats.averageAttempts() < 3) achievements.add("Speed Solver");

        String report = ReportGenerator.generatePlayerReport(new PlayerReportData(
            "player-" + playerName.toLowerCase(),
            playerName.toLowerCase() + "@example.com",
            playerName,
            stats.gamesPlayed(),
            stats.gamesWon(),
            stats.winRate(),
            stats.averageAttempts(),
            fastestWin,
            dates.get(0),
            dates.get(dates.size() - 1),
            playerRank.rank,
            percentile,
            achievements
        ));

        System.out.println("\n" + report + "\n");
    }

    private static List<RankedPlayer> rankPlayers(List<PlayerRank> playerStatsList) {
        List<PlayerRank> sorted = playerStatsList.stream()
            .sorted(Comparator.comparing((PlayerRank p) -> p.stats.winRate()).reversed()
                .thenComparing(p -> p.stats.gamesWon(), Comparator.reverseOrder()))
            .toList();

        List<RankedPlayer> result = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            result.add(new RankedPlayer(sorted.get(i).playerName, i + 1));
        }
        return result;
    }

    public static void showHelp() {
        System.out.println("""

Wordle Stats CLI
================

Commands:
  mvn exec:java -Dexec.mainClass="com.skillerwhale.wordlestats.cli.Main" -Dexec.args="list"              List all game results
  mvn exec:java -Dexec.mainClass="com.skillerwhale.wordlestats.cli.Main" -Dexec.args="stats <player>"    Show stats for a specific player
  mvn exec:java -Dexec.mainClass="com.skillerwhale.wordlestats.cli.Main" -Dexec.args="report <player>"   Generate full player report
  mvn exec:java -Dexec.mainClass="com.skillerwhale.wordlestats.cli.Main" -Dexec.args="help"              Show this help message

Interactive mode:
  mvn exec:java -Dexec.mainClass="com.skillerwhale.wordlestats.cli.Main"                                 Start interactive menu

Available players: """ + String.join(", ", getPlayerNames()) + "\n");
    }

    public static void showMenu() {
        System.out.println("""

╔═════════════════════════════════════╗
║     Wordle Stats - Main Menu        ║
╠═════════════════════════════════════╣
║  1. List all games                  ║
║  2. Show player stats               ║
║  3. Generate player report          ║
║  0. Exit                            ║
╚═════════════════════════════════════╝
""");
    }

    public static void runInteractive() {
        // Set console encoding to UTF-8 for emoji support
        System.setProperty("file.encoding", "UTF-8");
        System.setOut(new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    listGames();
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;
                case "2":
                    System.out.println("\nAvailable players: " + String.join(", ", getPlayerNames()));
                    System.out.print("Enter player name: ");
                    String player2 = scanner.nextLine().trim();
                    if (!player2.isEmpty()) {
                        showPlayerStats(player2);
                    }
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;
                case "3":
                    System.out.println("\nAvailable players: " + String.join(", ", getPlayerNames()));
                    System.out.print("Enter player name: ");
                    String player3 = scanner.nextLine().trim();
                    if (!player3.isEmpty()) {
                        showPlayerReport(player3);
                    }
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;
                case "0":
                    System.out.println("\nGoodbye!\n");
                    return;
                default:
                    System.out.println("\nInvalid option. Please try again.\n");
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;
            }
        }
    }

    // Helper record classes
    private record PlayerRank(String playerName, PlayerStatsData stats) {}
    private record RankedPlayer(String playerName, int rank) {}
}
