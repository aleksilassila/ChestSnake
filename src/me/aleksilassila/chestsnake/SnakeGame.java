package me.aleksilassila.chestsnake;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleksilassila.chestsnake.GUIs.GUI;
import me.aleksilassila.chestsnake.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SnakeGame extends BukkitRunnable {
    private final Player player;
    private final Gui gui;
    private final StaticPane gamePane;

    public final int GAME_WIDTH = 9 - 1;
    public final int GAME_HEIGHT = (ChestSnake.instance.getConfig().getBoolean("biggerDisplay", false) ? 6 : 4) - 1;
    private final long DELAY = Math.round(20 / (double) ChestSnake.instance.getConfig().getInt("speed"));
    private final int TIME_BONUS = ChestSnake.instance.getConfig().getInt("timeBonus");
    private final int TIME_BONUSES_PER_FOOD = ChestSnake.instance.getConfig().getInt("timeBonusesPerFood");
    private final int FOOD_SCORE = ChestSnake.instance.getConfig().getInt("foodScore");
    private Material snakeMaterial;
    
    private int score = 0;
    private int timeBonusesLeft = TIME_BONUSES_PER_FOOD;

    private Direction direction = Direction.RIGHT;
    private Direction lastDirection = Direction.RIGHT;
    private int snakeLength = 2;
    private final int[] head = new int[]{1,0};
    private final ArrayList<int[]> body = new ArrayList<>(Collections.singletonList(new int[]{0, 0}));
    private int[] apple = getApple();

    public SnakeGame(Player player, Gui gui) {
        this.player = player;
        this.gui = gui;
        this.gamePane = new StaticPane(0, 0, GAME_WIDTH + 1, GAME_HEIGHT + 1);
        gui.addPane(gamePane);

        // Game background
        Material backgroundMaterial = parseMaterial(ChestSnake.instance.getConfig().getString("backgroundBlock"));

        if (backgroundMaterial != null) {
            OutlinePane background = new OutlinePane(0, 0, 9, GAME_HEIGHT + 1, Pane.Priority.LOWEST);
            background.addItem(new GuiItem(GUI.createGuiItem(backgroundMaterial, ChatColor.RESET + "", false)));
            background.setRepeat(true);

            gui.addPane(background);
        }

        this.snakeMaterial = parseMaterial(ChestSnake.instance.getConfig().getString("snakeBlock", "BUBBLE_CORAL_BLOCK"));

        if (snakeMaterial == null) {
            ChestSnake.instance.getLogger().severe("Invalid snake material");
            this.snakeMaterial = Material.BUBBLE_CORAL_BLOCK;
        }

        runTaskTimer(ChestSnake.instance, 0, DELAY);
    }

    public Player getPlayer() {
        return player;
    }

    public void setDirection(Direction newDirection) {
        if (newDirection.getAxis() != lastDirection.getAxis())
            direction = newDirection;
    }

    public void endGame() {
        Messages.send(player, "GAME_RESULT", score);

        if (ChestSnake.instance.getHighscore(player) < score) {
            ChestSnake.instance.setHighscore(player, score);
            Messages.send(player, "NEW_RECORD");
        }

        this.cancel();
        player.closeInventory();
    }

    private int[] getApple() {
        while (true) {
            int[] pos = new int[]{new Random().nextInt(GAME_WIDTH), new Random().nextInt(GAME_HEIGHT)};

            if (!containsPosition(body, pos) && !positionsEqual(head, pos))
                return pos;
        }
    }

    @Override
    public void run() { // Aka. Tick()
        // Update body
        body.add(new int[]{head[0], head[1]});
        if (body.size() + 1 > snakeLength) body.remove(0);

        // Move head
        if (direction == Direction.UP) head[1]--;
        else if (direction == Direction.DOWN) head[1]++;
        else if (direction == Direction.LEFT) head[0]--;
        else if (direction == Direction.RIGHT) head[0]++;

        // Check for collisions
        if (head[0] > GAME_WIDTH ||
                head[0] < 0 ||
                head[1] > GAME_HEIGHT ||
                head[1] < 0) {
            endGame();
            return;
        }

        if (containsPosition(body, head)) {
            endGame();
            return;
        }

        // Eat apple
        if (positionsEqual(head, apple)) {
            apple = getApple();
            snakeLength++;
            score += FOOD_SCORE + timeBonusesLeft * TIME_BONUS;
            timeBonusesLeft = TIME_BONUSES_PER_FOOD;
        } else {
            timeBonusesLeft = Math.max(0, timeBonusesLeft - 1);
        }

        lastDirection = direction;

        // Update and draw screen
        gamePane.clear();

        gamePane.addItem(new GuiItem(new ItemStack(snakeMaterial)), head[0], head[1]);
        for (int[] pos : body) {
            gamePane.addItem(new GuiItem(new ItemStack(snakeMaterial)), pos[0], pos[1]);
        }
        gamePane.addItem(new GuiItem(new ItemStack(Material.APPLE)), apple[0], apple[1]);

        gui.update();
    }

    @Nullable
    private Material parseMaterial(String materialString) {
        try {
            return Material.valueOf(materialString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean containsPosition(ArrayList<int[]> list, int[] pos) {
        for (int[] item : list) {
            if (item[0] == pos[0] && item[1] == pos[1]) return true;
        }

        return false;
    }

    private boolean positionsEqual(int[] pos1, int[] pos2) {
        return pos1[0] == pos2[0] && pos1[1] == pos2[1];
    }

     public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;

        public int getAxis() {
            if (this == UP || this == DOWN) return 1;
            else return 0;
        }
    }
}
