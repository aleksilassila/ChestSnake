package me.aleksilassila.chestsnake.GUIs;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.aleksilassila.chestsnake.SnakeGame;
import me.aleksilassila.chestsnake.SnakeGame.Direction;
import me.aleksilassila.chestsnake.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayGUI extends GUI {
    private final Player player;
    private final Gui gui;
    private final SnakeGame game;

    public PlayGUI(Player player) {
        this.player = player;
        this.gui = getMainGui();
        this.game = new SnakeGame(player, gui);

        gui.show(player);
    }

    @Override
    public Gui getMainGui() {
        Gui gui = new Gui(6, Messages.get("gui.play.TITLE"));
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        gui.setOnClose(event -> game.endGame());

        StaticPane controls = new StaticPane(0, 4, 3, 2);

        controls.addItem(new GuiItem(createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "Left", true), inventoryClickEvent -> {
            game.setDirection(Direction.LEFT);
        }), 0, 1);

        controls.addItem(new GuiItem(createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "Right", true), inventoryClickEvent -> {
            game.setDirection(Direction.RIGHT);
        }), 2, 1);

        controls.addItem(new GuiItem(createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "Up", true), inventoryClickEvent -> {
            game.setDirection(Direction.UP);
        }), 1, 0);

        controls.addItem(new GuiItem(createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "Down", true), inventoryClickEvent -> {
            game.setDirection(Direction.DOWN);
        }), 1, 1);

        gui.addPane(controls);

        return gui;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
