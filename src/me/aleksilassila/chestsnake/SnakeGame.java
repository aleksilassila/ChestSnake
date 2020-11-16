package me.aleksilassila.chestsnake;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SnakeGame extends BukkitRunnable {
    private final Player player;
    private final Gui gui;
    private final StaticPane gamePane;

    public final int GAME_WIDTH = 9 - 1;
    public final int GAME_HEIGHT = 6 - 1;

    private boolean running = true;
    private Direction direction = Direction.LEFT;
    private final int[] head = new int[]{4,4};
    private final ArrayList<int[]> body = new ArrayList<>();
    private final int[] apple = new int[]{-1, -1};

    public SnakeGame(Player player, Gui gui) {
        this.player = player;
        this.gui = gui;
        this.gamePane = new StaticPane(0, 0, GAME_WIDTH + 1, GAME_HEIGHT + 1);
        gui.addPane(gamePane);

        runTaskTimer(ChestSnake.instance, 20, 10);
    }

    public Player getPlayer() {
        return player;
    }

    public int[] getHead() {
        return head;
    }

    public boolean isRunning() {
        return running;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void endGame() {
        running = false;
        this.cancel();
    }

    @Override
    public void run() { // Aka. Tick()
        // Move snake
        switch (direction) {
            case UP:
                head[1]--;
                break;
            case DOWN:
                head[1]++;
                break;
            case LEFT:
                head[0]--;
                break;
            case RIGHT:
                head[0]++;
                break;
        }

        // Check for collisions
        if (head[0] > GAME_WIDTH ||
                head[0] < 0 ||
                head[1] > GAME_HEIGHT ||
                head[1] < 0) {
            endGame();
            return;
        }

        // Update screen
        gamePane.clear();

        // Draw everything
        gamePane.addItem(new GuiItem(new ItemStack(Material.GREEN_STAINED_GLASS)), head[0], head[1]);
        for (int[] pos : body) {
            gamePane.addItem(new GuiItem(new ItemStack(Material.GREEN_STAINED_GLASS)), pos[0], pos[1]);
        }
        gamePane.addItem(new GuiItem(new ItemStack(Material.APPLE)), apple[0], apple[1]);

        gui.update();
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }
}
