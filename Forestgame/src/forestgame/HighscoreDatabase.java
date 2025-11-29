package forestgame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite-based highscore storage.
 *
 * DB file: forest_escape.db
 * Table : highscores
 */
public class HighscoreDatabase {

    private static final String DB_URL = "jdbc:sqlite:forest_escape.db";

    static {
        try {
             Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
        }
        initDatabase();
    }

    private static void initDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS highscores (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    player_name  TEXT NOT NULL,
                    level_name   TEXT NOT NULL,
                    mushrooms    INTEGER NOT NULL,
                    time_seconds INTEGER NOT NULL,
                    created_at   TEXT DEFAULT CURRENT_TIMESTAMP
                );
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to init highscores table: " + e.getMessage());
        }
    }

    /**
     * Save one score row into DB.
     */
    public static void saveScore(String playerName,
                                 String levelName,
                                 int mushrooms,
                                 int timeSeconds) {

        String sql = """
                INSERT INTO highscores (player_name, level_name, mushrooms, time_seconds)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, playerName);
            ps.setString(2, levelName);
            ps.setInt(3, mushrooms);
            ps.setInt(4, timeSeconds);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to save highscore: " + e.getMessage());
        }
    }

    /**
     * Load top N scores for a specific level.
     * Ordered by:
     *   1) mushrooms DESC
     *   2) time_seconds ASC
     */
    public static List<HighscoreEntry> loadTopScores(String levelName, int limit) {

        List<HighscoreEntry> result = new ArrayList<>();

        String sql = """
                SELECT player_name, level_name, mushrooms, time_seconds, created_at
                FROM highscores
                WHERE level_name = ?
                ORDER BY mushrooms DESC, time_seconds ASC
                LIMIT ?
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, levelName);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String playerName = rs.getString("player_name");
                    String lvlName    = rs.getString("level_name");
                    int mushrooms     = rs.getInt("mushrooms");
                    int timeSeconds   = rs.getInt("time_seconds");
                    String createdAt  = rs.getString("created_at");

                    result.add(new HighscoreEntry(
                            playerName, lvlName, mushrooms, timeSeconds, createdAt
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Failed to load highscores: " + e.getMessage());
        }

        return result;
    }
}
 