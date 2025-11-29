package forestgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the wolf (enemy) with position and movement direction.
 */
public class Wolf {

    private int row;
    private int col;
    private Direction direction;

    /**
     * Creates a wolf at the given starting position.
     *
     * @param startRow 
     * @param startCol 
     */
    public Wolf(int startRow, int startCol) {
        this.row = startRow;
        this.col = startCol;
        this.direction = Direction.LEFT; // arbitrary default
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

    public Direction getDirection() {
        return direction;
    }

    /**
     * Moves the wolf one step, possibly changing direction if blocked.
     *
     * @param map  
     * @param rand  
     */
    public void update(GameMap map, Random rand) {
        // Try moving in the current direction
        int nextRow = row + direction.dRow;
        int nextCol = col + direction.dCol;

        if (map.isWalkableForWolf(nextRow, nextCol)) {
            row = nextRow;
            col = nextCol;
            return;
        }

        // If blocked, choose a new random direction from possible options
        List<Direction> options = new ArrayList<>();
        for (Direction d : Direction.values()) {
            int nr = row + d.dRow;
            int nc = col + d.dCol;
            if (map.isWalkableForWolf(nr, nc)) {
                options.add(d);
            }
        }

        if (!options.isEmpty()) {
            direction = options.get(rand.nextInt(options.size()));
            row += direction.dRow;
            col += direction.dCol;
        }
        // If no valid directions, wolf stays in place.
    }
    public void setDirection(Direction dir) {
        this.direction = dir;
    }
}
