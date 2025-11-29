package forestgame;

/**
 * Represents one highscore row from the database.
 */
public class HighscoreEntry {

    private final String playerName;
    private final String levelName;
    private final int mushrooms;
    private final int timeSeconds;
    private final String createdAt;  

    public HighscoreEntry(String playerName,
                          String levelName,
                          int mushrooms,
                          int timeSeconds,
                          String createdAt) {
        this.playerName = playerName;
        this.levelName = levelName;
        this.mushrooms = mushrooms;
        this.timeSeconds = timeSeconds;
        this.createdAt = createdAt;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getMushrooms() {
        return mushrooms;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
