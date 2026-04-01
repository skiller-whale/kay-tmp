package com.skillerwhale.wordle;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import com.skillerwhale.wordle.core.GameState;

public class Database implements GameRepository {
    private static final String DB_URL = "jdbc:sqlite:games.db";
    private Connection connection;

    public Database() {
        try {
            // Explicitly load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private void initializeDatabase() throws SQLException {
        // Simulate slow database initialization for testing purposes
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Database initialization interrupted", e);
        }

        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS games (
                id TEXT PRIMARY KEY,
                correct_answer TEXT NOT NULL,
                guesses TEXT NOT NULL,
                evaluations TEXT NOT NULL,
                error TEXT
            )
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    public void saveGame(GameState game) {
        String sql = """
            INSERT OR REPLACE INTO games (id, correct_answer, guesses, evaluations, error)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, game.getId());
            pstmt.setString(2, game.getCorrectAnswer());
            pstmt.setString(3, serializeGuesses(game.getGuesses()));
            pstmt.setString(4, serializeEvaluations(game.getEvaluations()));
            pstmt.setString(5, game.getError());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save game", e);
        }
    }

    public GameState getGame(String id) {
        String sql = "SELECT * FROM games WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return deserializeGameState(id, rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve game", e);
        }

        return null;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Helper methods for serializing/deserializing lists
    // (you can ignore everything below this line for the exercises)
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private String serializeGuesses(List<String> guesses) {
        return String.join(",", guesses);
    }

    private String serializeEvaluations(List<List<String>> evaluations) {
        List<String> serializedEvaluations = new ArrayList<>();
        for (List<String> evaluation : evaluations) {
            serializedEvaluations.add(String.join("|", evaluation));
        }
        return String.join(";", serializedEvaluations);
    }

    private List<String> deserializeGuesses(String guessesStr) {
        if (guessesStr == null || guessesStr.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(List.of(guessesStr.split(",")));
    }

    private List<List<String>> deserializeEvaluations(String evaluationsStr) {
        if (evaluationsStr == null || evaluationsStr.isEmpty()) {
            return new ArrayList<>();
        }

        List<List<String>> evaluations = new ArrayList<>();
        String[] evaluationStrs = evaluationsStr.split(";");
        for (String evalStr : evaluationStrs) {
            evaluations.add(new ArrayList<>(List.of(evalStr.split("\\|"))));
        }
        return evaluations;
    }

    private GameState deserializeGameState(String id, ResultSet rs) throws SQLException {
        // Create a new GameState with the correct answer
        String correctAnswer = rs.getString("correct_answer");
        String error = rs.getString("error");
        GameState game = new GameState(id, correctAnswer, error);

        // We need to set the private fields, but since GameState doesn't have setters,
        // we'll need to recreate the state by making the guesses
        List<String> guesses = deserializeGuesses(rs.getString("guesses"));

        // Make all the guesses to recreate the state
        for (String guess : guesses) {
            game.makeGuess(guess);
        }

        return game;
    }
}
