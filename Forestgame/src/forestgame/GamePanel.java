package forestgame;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.List;

public class GamePanel extends JPanel {

    private static final int TILE_SIZE = 32;
    private static final int GRID_SIZE = 20;
    private static final int HUD_HEIGHT = 24;

    private static final int PANEL_WIDTH = GRID_SIZE * TILE_SIZE;
    private static final int PANEL_HEIGHT =
            HUD_HEIGHT + GRID_SIZE * TILE_SIZE;

    private static final int TIMER_DELAY_MS = 1000;
    private static final int POWERUP_DURATION_SECONDS = 5;

    private GameState gameState;
    private Timer gameTimer;
    private final Random random;

    private String currentLevelPath;
    
    public GamePanel() {

        this.random = new Random();

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);

        setFocusable(true);
        setRequestFocusEnabled(true);

        setupKeyBindings();

        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }
    
    public void setCurrentLevelPath(String path) {
        this.currentLevelPath = path;
    }
    
    public void startGame() {
        if (currentLevelPath == null) {
            JOptionPane.showMessageDialog(this,
                    "No level selected.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

         String playerName = null;
        while (playerName == null || playerName.isBlank()) {
            playerName = JOptionPane.showInputDialog(this, "Enter your player name:");
            if (playerName == null) {
                 return;
            }
        }

        startNewGame(currentLevelPath, playerName);
    }
    /**
     * @param path
     * @param playerName 
     */
    public void startNewGame(String path, String playerName) {
        try {
            LoadedLevel ll = LevelLoader.loadFromFile(path);
            this.gameState = new GameState(ll);
            this.gameState.setPlayerName(playerName);

            restartTimer();

            SwingUtilities.invokeLater(this::requestFocusInWindow);

            repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load level:\n" + ex.getMessage(),
                    "Level Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void restartTimer() {
        if (gameTimer != null)
            gameTimer.stop();

        if (gameState == null)
            return;

        gameState.resetElapsedSeconds();
        gameState.setWolfLastMoveSecond(0);
        gameState.setGameOver(false);

        gameTimer = new Timer(TIMER_DELAY_MS, (ActionEvent e) -> {

            if (gameState == null || gameState.isGameOver()) {
                gameTimer.stop();
                return;
            }

            gameState.incrementElapsedSeconds();
            updatePowerUps();
            updateWolfMovement();
            checkWolfCollision();

            updateWindowTitle();
            repaint();
        });

        gameTimer.start();
        updateWindowTitle();
    }
    
    private void updatePowerUps() {
        Player p = gameState.getPlayer();
        int now = gameState.getElapsedSeconds();

        if (p.isSpeedActive() &&
                now >= p.getSpeedExpiresAtSecond())
            p.deactivateSpeed();

        if (p.isInvisActive() &&
                now >= p.getInvisExpiresAtSecond())
            p.deactivateInvisibility();
    }
    
    private void updateWolfMovement() {
        Player player = gameState.getPlayer();
        GameMap map = gameState.getMap();
        List<Wolf> wolves = gameState.getWolves();

        int now = gameState.getElapsedSeconds();
        int lastMove = gameState.getWolfLastMoveSecond();

        int interval = player.isSpeedActive() ? 2 : 1;

        if (now - lastMove >= interval) {

            for (Wolf w : wolves)
                w.update(map, random);

            gameState.setWolfLastMoveSecond(now);
        }
    }
    
    private void checkWolfCollision() {
        Player p = gameState.getPlayer();
        List<Wolf> wolves = gameState.getWolves();

        if (p.isInvisActive())
            return;

        int pr = p.getRow();
        int pc = p.getCol();

        boolean hit = false;

        for (Wolf w : wolves) {
            int wr = w.getRow();
            int wc = w.getCol();

            boolean same = (pr == wr && pc == wc);
            boolean adj = (Math.abs(pr - wr) + Math.abs(pc - wc) == 1);

            if (same || adj) {
                hit = true;
                break;
            }
        }

        if (!hit)
            return;

        p.decrementLives();

        if (p.getLives() > 0) {

            int sr = gameState.getMap().getCampfireRow();
            int sc = gameState.getMap().getCampfireCol();
            p.respawn(sr, sc);

            JOptionPane.showMessageDialog(this,
                    "Wolves caught you! Lives left: " + p.getLives(),
                    "Caught!",
                    JOptionPane.WARNING_MESSAGE);

        } else {
            handleGameOver();
        }
    }
    
    private void handleGameOver() {
        gameState.setGameOver(true);
        if (gameTimer != null)
            gameTimer.stop();

        Player p = gameState.getPlayer();
        String levelName = gameState.getLevelName();
        String playerName = gameState.getPlayerName();

         HighscoreDatabase.saveScore(
                playerName,
                levelName,
                p.getMushroomsCollected(),
                gameState.getElapsedSeconds()
        );

        JOptionPane.showMessageDialog(this,
                "GAME OVER\nMushrooms: " + p.getMushroomsCollected()
                        + "\nTime: " + gameState.getElapsedSeconds(),
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setupKeyBindings() {

        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, 0), "up");
        im.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, 0), "down");
        im.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, 0), "left");
        im.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, 0), "right");

        am.put("up", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                handlePlayerMove(-1, 0);
            }
        });
        am.put("down", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                handlePlayerMove(1, 0);
            }
        });
        am.put("left", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                handlePlayerMove(0, -1);
            }
        });
        am.put("right", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                handlePlayerMove(0, 1);
            }
        });
    }
    /**
     * @param dr
     * @param dc 
     */
    private void handlePlayerMove(int dr, int dc) {

        if (gameState == null || gameState.isGameOver())
            return;

        Player p = gameState.getPlayer();
        GameMap m = gameState.getMap();

        boolean moved = p.moveBy(dr, dc, m);
        if (!moved)
            return;

        int r = p.getRow();
        int c = p.getCol();
        TileType t = m.getTile(r, c);

 
        if (t == TileType.MUSHROOM) {

            m.setTile(r, c, TileType.GROUND);
            p.incrementMushroomsCollected();
            repaint(); // show removal

            if (!m.hasAnyMushrooms()) {

                if (gameTimer != null)
                    gameTimer.stop();

                 HighscoreDatabase.saveScore(
                        gameState.getPlayerName(),
                        gameState.getLevelName(),
                        p.getMushroomsCollected(),
                        gameState.getElapsedSeconds()
                );

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Level Cleared!\nMushrooms: "
                                    + p.getMushroomsCollected()
                                    + "\nTime: " + gameState.getElapsedSeconds(),
                            "Level Cleared",
                            JOptionPane.INFORMATION_MESSAGE);

                     startNewGame(currentLevelPath, gameState.getPlayerName());
                });

                return;
            }
        }

         if (t == TileType.SPEED_POWERUP) {
            m.setTile(r, c, TileType.GROUND);
            p.activateSpeed(gameState.getElapsedSeconds(), POWERUP_DURATION_SECONDS);
        } else if (t == TileType.INVIS_POWERUP) {
            m.setTile(r, c, TileType.GROUND);
            p.activateInvisibility(gameState.getElapsedSeconds(), POWERUP_DURATION_SECONDS);
        } else if (t == TileType.EXTRA_LIFE) {
            m.setTile(r, c, TileType.GROUND);
            p.incrementLives();
        }

         checkWolfCollision();

        updateWindowTitle();
        repaint();
    }
    
    private void updateWindowTitle() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (!(w instanceof javax.swing.JFrame frame))
            return;

        Player p = gameState.getPlayer();

        String title = "Forest Escape - " + gameState.getLevelName()
                + " | Player: " + gameState.getPlayerName()
                + " | Lives: " + p.getLives()
                + " | Mushrooms: " + p.getMushroomsCollected()
                + " | Time: " + gameState.getElapsedSeconds();

        if (p.isSpeedActive())
            title += " | SPEED";

        if (p.isInvisActive())
            title += " | INVISIBLE";

        frame.setTitle(title);
    }
    /**
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (gameState == null) {
            g.setColor(Color.WHITE);
            g.drawString("Select a level and press Start Game.", 20, 40);
            return;
        }

        GameMap map = gameState.getMap();
        Player player = gameState.getPlayer();
        List<Wolf> wolves = gameState.getWolves();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // HUD
        g2.setColor(Color.WHITE);
        g2.drawString("Lives: " + player.getLives()
                        + "  Mushrooms: " + player.getMushroomsCollected()
                        + "  Time: " + gameState.getElapsedSeconds(),
                10, 16);

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {

                int x = c * TILE_SIZE;
                int y = HUD_HEIGHT + r * TILE_SIZE;

                if (!isTileVisible(r, c, player)) {
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    continue;
                }

                TileType t = map.getTile(r, c);

                g2.setColor(new Color(34, 139, 34));
                g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                switch (t) {
                    case TREE -> {
                        g2.setColor(new Color(0, 80, 0));
                        g2.fillRect(x + 4, y + 4, TILE_SIZE - 8, TILE_SIZE - 8);
                    }
                    case ROCK -> {
                        g2.setColor(Color.GRAY);
                        g2.fillOval(x + 5, y + 5,
                                TILE_SIZE - 10, TILE_SIZE - 10);
                    }
                    case BUSH -> {
                        g2.setColor(new Color(0, 150, 0));
                        g2.fillOval(x + 3, y + 3,
                                TILE_SIZE - 6, TILE_SIZE - 6);
                    }
                    case CAMPFIRE -> {
                        g2.setColor(new Color(139, 69, 19));
                        g2.fillRect(x + 6, y + TILE_SIZE - 8,
                                TILE_SIZE - 12, 4);
                        g2.setColor(Color.ORANGE);
                        g2.fillOval(x + 8, y + 4,
                                TILE_SIZE - 16, TILE_SIZE - 8);
                    }
                    case MUSHROOM -> {
                        g2.setColor(Color.WHITE);
                        g2.fillRect(x + 10, y + 14,
                                TILE_SIZE - 20, TILE_SIZE - 18);
                        g2.setColor(Color.RED);
                        g2.fillOval(x + 6, y + 4,
                                TILE_SIZE - 12, TILE_SIZE - 16);
                    }
                    case SPEED_POWERUP -> {
                        g2.setColor(Color.YELLOW);
                        g2.fillOval(x + 8, y + 8,
                                TILE_SIZE - 16, TILE_SIZE - 16);
                        g2.setColor(Color.BLACK);
                        g2.drawString("S", x + TILE_SIZE / 2 - 3,
                                y + TILE_SIZE / 2 + 4);
                    }
                    case INVIS_POWERUP -> {
                        g2.setColor(Color.CYAN);
                        g2.fillOval(x + 8, y + 8,
                                TILE_SIZE - 16, TILE_SIZE - 16);
                        g2.setColor(Color.BLACK);
                        g2.drawString("I", x + TILE_SIZE / 2 - 3,
                                y + TILE_SIZE / 2 + 4);
                    }
                    case EXTRA_LIFE -> {
                        g2.setColor(Color.PINK);
                        g2.fillOval(x + 8, y + 8,
                                TILE_SIZE - 16, TILE_SIZE - 16);
                        g2.setColor(Color.BLACK);
                        g2.drawString("L", x + TILE_SIZE / 2 - 3,
                                y + TILE_SIZE / 2 + 4);
                    }
                }

                g2.setColor(new Color(0, 0, 0, 80));
                g2.drawRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }

        for (Wolf w : wolves) {
            if (isTileVisible(w.getRow(), w.getCol(), player)) {
                int x = w.getCol() * TILE_SIZE;
                int y = HUD_HEIGHT + w.getRow() * TILE_SIZE;
                g2.setColor(Color.RED);
                g2.fillOval(x + 6, y + 6,
                        TILE_SIZE - 12, TILE_SIZE - 12);
                g2.setColor(Color.WHITE);
                g2.drawString("W",
                        x + TILE_SIZE / 2 - 4,
                        y + TILE_SIZE / 2 + 5);
            }
        }

        if (isTileVisible(player.getRow(), player.getCol(), player)) {
            int px = player.getCol() * TILE_SIZE;
            int py = HUD_HEIGHT + player.getRow() * TILE_SIZE;
            g2.setColor(Color.BLUE);
            g2.fillOval(px + 6, py + 6,
                    TILE_SIZE - 12, TILE_SIZE - 12);
        }

        g2.dispose();
    }
    /**
     * @param r
     * @param c
     * @param p
     * @return 
     */
    private boolean isTileVisible(int r, int c, Player p) {
        int dr = Math.abs(r - p.getRow());
        int dc = Math.abs(c - p.getCol());
        return Math.max(dr, dc) <= 3;  
    }
}
