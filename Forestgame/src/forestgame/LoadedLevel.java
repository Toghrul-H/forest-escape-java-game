package forestgame;

import java.util.List;

public class LoadedLevel {

    private final GameMap map;
    private final Player player;
    private final List<Wolf> wolves;
    private final String levelName;
    /**
     * @param map
     * @param player
     * @param wolves
     * @param levelName 
     */
    public LoadedLevel(GameMap map, Player player, List<Wolf> wolves, String levelName) {
        this.map = map;
        this.player = player;
        this.wolves = wolves;
        this.levelName = levelName;
    }

    public GameMap getMap() { return map; }
    public Player getPlayer() { return player; }

    public List<Wolf> getWolves() { 
        return wolves; 
    }

    public String getLevelName() { 
        return levelName; 
    }
}
