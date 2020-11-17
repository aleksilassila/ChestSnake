package me.aleksilassila.chestsnake.GUIs;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleksilassila.chestsnake.ChestSnake;
import me.aleksilassila.chestsnake.SnakeGame;
import me.aleksilassila.chestsnake.SnakeGame.Direction;
import me.aleksilassila.chestsnake.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayGUI extends GUI {
    private final Player player;
    private Gui gameGui;
    private SnakeGame game;

    public PlayGUI(Player player) {
        this.player = player;

        getMainGui().show(player);
    }

    private Direction getHotbarButtonDirection(int i) {
        switch (i) {
            case 0:
                return Direction.LEFT;
            case 1:
                return Direction.UP;
            case 2:
                return Direction.RIGHT;
            default:
                return Direction.DOWN;
        }
    }

    @Override
    public Gui getMainGui() {
        Gui gui = new Gui(3, Messages.get("gui.play.TITLE"));
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        // Background
        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(createGuiItem(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.RESET + "", false)));
        background.setRepeat(true);

        gui.addPane(background);

        // Menu
        StaticPane menu = new StaticPane(2, 1, 5, 1);
        menu.addItem(new GuiItem(createGuiItem(Material.IRON_SWORD, Messages.get("gui.menu.PLAY"),
                true, Messages.get("gui.menu.PLAY_LORE")), inventoryClickEvent -> {
            this.gameGui = getGameGui();
            this.game = new SnakeGame(player, gameGui);

            gameGui.show(player);
        }), 0, 0);

        menu.addItem(new GuiItem(createGuiItem(Material.IRON_AXE, Messages.get("gui.menu.PLAY_CUSTOM"),
                true, Messages.get("gui.menu.PLAY_CUSTOM_LORE")), inventoryClickEvent -> {
            this.gameGui = getGameGui();
            this.game = new SnakeGame(player, gameGui);

            gameGui.show(player);
        }), 2, 0);

        menu.addItem(new GuiItem(createGuiItem(Material.COMPASS, Messages.get("gui.menu.HELP"),
                true, Messages.get("gui.menu.PLAY_CUSTOM_LORE")), inventoryClickEvent -> {
            this.gameGui = getGameGui();
            this.game = new SnakeGame(player, gameGui);

            gameGui.show(player);
        }), 4, 0);

        gui.addPane(menu);

        return gui;
    }

    public Gui getGameGui() {
        Gui gui = new Gui(6, Messages.get("gui.play.TITLE"));
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        gui.setOnClose(event -> {
            if (!game.isCancelled())
                game.endGame();
        });

        if (!ChestSnake.instance.getConfig().getBoolean("biggerDisplay", false)) {
            OutlinePane background = new OutlinePane(0, 4, 9, 2, Pane.Priority.LOWEST);
            background.addItem(new GuiItem(createGuiItem(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.RESET + "", false)));
            background.setRepeat(true);

            gui.addPane(background);
        }

        StaticPane controls = new StaticPane(3, 4, 3, 2, Pane.Priority.HIGHEST);

        controls.addItem(new GuiItem(createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "Left", true), inventoryClickEvent -> {
            if (inventoryClickEvent.getHotbarButton() == -1)
                game.setDirection(Direction.LEFT);
            else game.setDirection(getHotbarButtonDirection(inventoryClickEvent.getHotbarButton()));
        }), 0, 1);

        controls.addItem(new GuiItem(createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "Right", true), inventoryClickEvent -> {
            if (inventoryClickEvent.getHotbarButton() == -1)
                game.setDirection(Direction.RIGHT);
            else game.setDirection(getHotbarButtonDirection(inventoryClickEvent.getHotbarButton()));
        }), 2, 1);

        controls.addItem(new GuiItem(createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "Up", true), inventoryClickEvent -> {
            if (inventoryClickEvent.getHotbarButton() == -1)
                game.setDirection(Direction.UP);
            else game.setDirection(getHotbarButtonDirection(inventoryClickEvent.getHotbarButton()));
        }), 1, 0);

        controls.addItem(new GuiItem(createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "Down", true), inventoryClickEvent -> {
            if (inventoryClickEvent.getHotbarButton() == -1)
                game.setDirection(Direction.DOWN);
            else game.setDirection(getHotbarButtonDirection(inventoryClickEvent.getHotbarButton()));
        }), 1, 1);

        gui.addPane(controls);

        return gui;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
