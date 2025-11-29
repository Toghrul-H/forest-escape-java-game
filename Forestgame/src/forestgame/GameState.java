package forestgame;

import java.util.List;

/**
 * Holds the mutable runtime state of the current game session.
 */
public class GameState {

    private final GameMap map;
    private final Player player;
    private final List<Wolf> wolves;
    private final String levelName;

    private String playerName;  // for highscores

    private int elapsedSeconds;
    private int wolfLastMoveSecond;
    private boolean gameOver;
    /**
     * @param loaded 
     */
    public GameState(LoadedLevel loaded) {
        this.map = loaded.getMap();
        this.player = loaded.getPlayer();
        this.wolves = loaded.getWolves();
        this.levelName = loaded.getLevelName();
        this.elapsedSeconds = 0;
        this.wolfLastMoveSecond = 0;
        this.gameOver = false;
    }

    public GameMap getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Wolf> getWolves() {
        return wolves;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }
    
    public void incrementElapsedSeconds() {
        elapsedSeconds++;
    }
    
    public void resetElapsedSeconds() {
        elapsedSeconds = 0;
    }

    public int getWolfLastMoveSecond() {
        return wolfLastMoveSecond;
    }

    public void setWolfLastMoveSecond(int wolfLastMoveSecond) {
        this.wolfLastMoveSecond = wolfLastMoveSecond;
    }
    /**
     * @return 
     */
    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
