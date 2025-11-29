package forestgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    private static final int ROWS = 20;
    private static final int COLS = 20;
    /**
     * @param path
     * @return
     * @throws IOException 
     */
    public static LoadedLevel loadFromFile(String path) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(path));
        if (lines.size() != ROWS) {
            throw new IOException("Level must have exactly " + ROWS + " lines.");
        }

        TileType[][] tiles = new TileType[ROWS][COLS];

        int playerRow = -1;
        int playerCol = -1;
        int campfireRow = -1;
        int campfireCol = -1;

        List<Wolf> wolves = new ArrayList<>();

        for (int r = 0; r < ROWS; r++) {
            String line = lines.get(r);

            if (line.length() != COLS) {
                throw new IOException("Line " + (r + 1) + " must have length " + COLS);
            }

            for (int c = 0; c < COLS; c++) {
                char ch = line.charAt(c);
                TileType tile;

                switch (ch) {
                    case '#':
                    case 'T':
                        tile = TileType.TREE;
                        break;

                    case 'R':
                        tile = TileType.ROCK;
                        break;

                    case 'B':
                        tile = TileType.BUSH;
                        break;

                    case 'M':
                        tile = TileType.MUSHROOM;
                        break;

                    case 'C':
                        tile = TileType.CAMPFIRE;
                        campfireRow = r;
                        campfireCol = c;
                        break;

                    case 'S':
                        tile = TileType.SPEED_POWERUP;
                        break;

                    case 'I':
                        tile = TileType.INVIS_POWERUP;
                        break;

                    case 'L':
                        tile = TileType.EXTRA_LIFE;
                        break;

                    case 'P':
                        tile = TileType.GROUND;
                        playerRow = r;
                        playerCol = c;
                        break;

                    case 'W':
                        tile = TileType.GROUND;
                        wolves.add(new Wolf(r, c)); // multi-wolf
                        break;

                    case '.':
                    default:
                        tile = TileType.GROUND;
                        break;
                }

                tiles[r][c] = tile;
            }
        }

        if (playerRow < 0 || playerCol < 0)
            throw new IOException("Level missing player 'P'");

        if (campfireRow < 0 || campfireCol < 0)
            throw new IOException("Level missing campfire 'C'");

        if (wolves.isEmpty()) {
            wolves.add(new Wolf(campfireRow, Math.min(campfireCol + 1, COLS - 2)));
        }

        GameMap map = new GameMap(tiles, campfireRow, campfireCol);
        Player player = new Player(playerRow, playerCol);

        String levelName = extractLevelName(path);

        return new LoadedLevel(map, player, wolves, levelName);
    }

    /**
     * Convert file name like "level1.txt" to display name "Level 1".
     */
    private static String extractLevelName(String path) {
        String file = Paths.get(path).getFileName().toString();  
        int dotIndex = file.indexOf('.');
        String base = (dotIndex > 0) ? file.substring(0, dotIndex) : file;

        String lower = base.toLowerCase();
        if (lower.startsWith("level")) {
            String numberPart = base.substring(5); 
            return "Level " + numberPart;
        }
        return base;
    }
}
