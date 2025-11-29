package forestgame;

/**
 * Represents the game map (grid of tiles) with collision and helper methods.
 */
public class GameMap {

    private final TileType[][] tiles;
    private final int rows;
    private final int cols;

    private int campfireRow;
    private int campfireCol;

    /**
     * Creates a GameMap with the given tile array and campfire position.
     *
     * @param tiles       
     * @param campfireRow 
     * @param campfireCol 
     */
    public GameMap(TileType[][] tiles, int campfireRow, int campfireCol) {
        this.tiles = tiles;
        this.rows = tiles.length;
        this.cols = tiles[0].length;
        this.campfireRow = campfireRow;
        this.campfireCol = campfireCol;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public TileType getTile(int row, int col) {
        return tiles[row][col];
    }

    public void setTile(int row, int col, TileType tileType) {
        tiles[row][col] = tileType;
    }

    public int getCampfireRow() {
        return campfireRow;
    }

    public int getCampfireCol() {
        return campfireCol;
    }

    /**
     * Checks if a position is inside the map.
     * @param row 
     * @param col 
     * @return 
     */
    public boolean isInside(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Returns true if the target tile is walkable by the player.
     *
     * @param row 
     * @param col 
     * @return 
     */
    public boolean isWalkableForPlayer(int row, int col) {
        if (!isInside(row, col)) {
            return false;
        }
        return !getTile(row, col).isBlockingForPlayer();
    }

    /**
     * Returns true if the target tile is walkable by the wolf.
     *
     * @param row 
     * @param col 
     * @return 
     */
    public boolean isWalkableForWolf(int row, int col) {
        if (!isInside(row, col)) {
            return false;
        }
        return !getTile(row, col).isBlockingForWolf();
    }

    /**
     * @return 
     */
    public boolean hasAnyMushrooms() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (tiles[r][c] == TileType.MUSHROOM) {
                    return true;
                }
            }
        }
        return false;
    }
}
