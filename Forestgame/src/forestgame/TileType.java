package forestgame;

/**
 * Types of tiles used in the Forest Escape level map.
 */
public enum TileType {
    GROUND,
    TREE,
    ROCK,
    BUSH,
    CAMPFIRE,
    MUSHROOM,
    SPEED_POWERUP,
    INVIS_POWERUP,
    EXTRA_LIFE;

    /**
     * Returns whether this tile blocks movement for the player.
     * @return 
     */
    
    public boolean isBlockingForPlayer() {
        return this == TREE || this == ROCK || this == BUSH;
    }

    /**
     * Returns whether this tile blocks movement for the wolf.
     * Campfire also blocks the wolf.
     *
     * @return 
     */
    public boolean isBlockingForWolf() {
        return this == TREE || this == ROCK || this == BUSH || this == CAMPFIRE;
    }

    /**
     * @return 
     */
    public boolean isMushroom() {
        return this == MUSHROOM;
    }

    /**
     * @return 
     */
    public boolean isPowerUp() {
        return this == SPEED_POWERUP || this == INVIS_POWERUP || this == EXTRA_LIFE;
    }
}
