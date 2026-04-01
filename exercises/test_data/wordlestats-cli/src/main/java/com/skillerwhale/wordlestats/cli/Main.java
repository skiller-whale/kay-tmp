package com.skillerwhale.wordlestats.cli;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            // Interactive mode
            Commands.runInteractive();
        } else {
            // Command-line mode
            String command = args[0];

            switch (command) {
                case "list":
                    Commands.listGames();
                    break;
                case "stats":
                    if (args.length > 1) {
                        Commands.showPlayerStats(args[1]);
                    } else {
                        System.out.println("\nError: Please specify a player name\n");
                        System.out.println("Available players: " + String.join(", ", Commands.getPlayerNames()) + "\n");
                    }
                    break;
                case "report":
                    if (args.length > 1) {
                        Commands.showPlayerReport(args[1]);
                    } else {
                        System.out.println("\nError: Please specify a player name\n");
                        System.out.println("Available players: " + String.join(", ", Commands.getPlayerNames()) + "\n");
                    }
                    break;
                case "help":
                    Commands.showHelp();
                    break;
                default:
                    System.out.println("\nUnknown command: " + command + "\n");
                    Commands.showHelp();
                    break;
            }
        }
    }
}
