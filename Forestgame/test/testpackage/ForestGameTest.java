package testpackage;

// Import all game classes
import forestgame.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

/**
 * Tests for Forest Escape core logic (non-GUI).
 * This version uses JUnit 4 (junit-4.13.2.jar + hamcrest-core-1.3.jar).
 */
public class ForestGameTest {

    // ---------------------------
    // 1. LEVEL LOADING TEST
    // ---------------------------
    @Test
    public void testLevelLoaderLoadsCorrectly() throws IOException {

        LoadedLevel level = LevelLoader.loadFromFile("levels/level1.txt");

        assertNotNull(level);
        assertNotNull(level.getMap());
        assertNotNull(level.getPlayer());
        assertFalse(level.getWolves().isEmpty());
        assertEquals(20, level.getMap().getRows());
        assertEquals(20, level.getMap().getCols());
    }

    // ---------------------------
    // 2. PLAYER MOVEMENT TESTS
    // ---------------------------
    @Test
    public void testPlayerMovementBlockedByTree() {

        TileType[][] tiles = {
                {TileType.GROUND, TileType.TREE},
                {TileType.GROUND, TileType.GROUND}
        };

        GameMap map = new GameMap(tiles, 0, 0);
        Player player = new Player(0, 0);

        boolean moved = player.moveBy(0, 1, map);
        assertFalse(moved);
        assertEquals(0, player.getCol());
    }

    @Test
    public void testPlayerMovementIntoGround() {

        TileType[][] tiles = {
                {TileType.GROUND, TileType.GROUND}
        };

        GameMap map = new GameMap(tiles, 0, 0);
        Player player = new Player(0, 0);

        boolean moved = player.moveBy(0, 1, map);
        assertTrue(moved);
        assertEquals(1, player.getCol());
    }

    // ---------------------------
    // 3. WOLF MOVEMENT TESTS
    // ---------------------------
    @Test
    public void testWolfStopsAtObstacle() {

        TileType[][] tiles = {
                {TileType.GROUND, TileType.TREE, TileType.GROUND}
        };

        GameMap map = new GameMap(tiles, 0, 0);
        Wolf wolf = new Wolf(0, 0);

        wolf.setDirection(Direction.RIGHT);
        wolf.update(map, new java.util.Random(0));

        // Should NOT move into tree at (0,1)
        assertEquals(0, wolf.getCol());
    }

    @Test
    public void testWolfMovesForwardCorrectly() {

        TileType[][] tiles = {
                {TileType.GROUND, TileType.GROUND}
        };

        GameMap map = new GameMap(tiles, 0, 0);
        Wolf wolf = new Wolf(0, 0);

        wolf.setDirection(Direction.RIGHT);
        wolf.update(map, new java.util.Random(0));

        assertEquals(1, wolf.getCol());
    }

    // ---------------------------
    // 4. DATABASE TEST
    // ---------------------------
    @Test
    public void testHighscoreDatabaseInsertAndRetrieve() {

        HighscoreDatabase.saveScore("TestPlayer", "Level 1", 5, 20);

        List<HighscoreEntry> scores =
                HighscoreDatabase.loadTopScores("Level 1", 10);

        assertFalse(scores.isEmpty());

        HighscoreEntry first = scores.get(0);

        assertEquals("Level 1", first.getLevelName());
        assertTrue(first.getMushrooms() >= 0);
        assertTrue(first.getTimeSeconds() >= 0);
    }
}
