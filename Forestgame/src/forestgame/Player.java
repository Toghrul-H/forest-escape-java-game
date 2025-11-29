package forestgame;

/**
 * Represents the player: position, lives, score, and power-ups.
 */
public class Player {

    public static final int MAX_LIVES = 5;

    private int row;
    private int col;
    private int lives;
    private int mushroomsCollected;

    private boolean speedActive;
    private boolean invisActive;
    private int speedExpiresAtSecond;
    private int invisExpiresAtSecond;

    /**
     * Creates a new player at the given starting position with 3 lives.
     *
     * @param startRow 
     * @param startCol 
     */
    public Player(int startRow, int startCol) {
        this.row = startRow;
        this.col = startCol;
        this.lives = 3;
        this.mushroomsCollected = 0;
        this.speedActive = false;
        this.invisActive = false;
    }

    /**
     * Attempts to move the player by the given delta if the target tile is walkable.
     *
     * @param dRow 
     * @param dCol
     * @param map  
     * @return 
     */
    public boolean moveBy(int dRow, int dCol, GameMap map) {
        int newRow = row + dRow;
        int newCol = col + dCol;

        if (map.isWalkableForPlayer(newRow, newCol)) {
            this.row = newRow;
            this.col = newCol;
            return true;
        }
        return false;
    }

    /**
     * Respawns the player at the given coordinates.
     *
     * @param spawnRow 
     * @param spawnCol 
     */
    public void respawn(int spawnRow, int spawnCol) {
        this.row = spawnRow;
        this.col = spawnCol;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getLives() {
        return lives;
    }
    
    public void decrementLives() {
        lives = Math.max(0, lives - 1);
    }
    
    public void incrementLives() {
        if (lives < MAX_LIVES) {
            lives++;
        }
    }
    /**
     * @return 
     */
    public int getMushroomsCollected() {
        return mushroomsCollected;
    }
    
    public void incrementMushroomsCollected() {
        mushroomsCollected++;
    }
    /**
     * @return 
     */
    public boolean isSpeedActive() {
        return speedActive;
    }
    /**
     * @param currentSecond
     * @param durationSeconds 
     */
    public void activateSpeed(int currentSecond, int durationSeconds) {
        this.speedActive = true;
        this.speedExpiresAtSecond = currentSecond + durationSeconds;
    }
    
    public void deactivateSpeed() {
        this.speedActive = false;
    }
    /**
     * @return 
     */
    public int getSpeedExpiresAtSecond() {
        return speedExpiresAtSecond;
    }
    /**
     * @return 
     */
    public boolean isInvisActive() {
        return invisActive;
    }
    /**
     * @param currentSecond
     * @param durationSeconds 
     */
    public void activateInvisibility(int currentSecond, int durationSeconds) {
        this.invisActive = true;
        this.invisExpiresAtSecond = currentSecond + durationSeconds;
    }
    
    public void deactivateInvisibility() {
        this.invisActive = false;
    }
    /**
     * @return 
     */
    public int getInvisExpiresAtSecond() {
        return invisExpiresAtSecond;
    }
}
