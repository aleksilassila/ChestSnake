package me.aleksilassila.chestsnake;

import me.aleksilassila.chestsnake.commands.ChestSnakeCommand;
import me.aleksilassila.chestsnake.utils.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestSnake extends JavaPlugin {
    public static ChestSnake instance;

    @Override
    public void onEnable() {
        instance = this;

        Messages.init();

        new ChestSnakeCommand();
    }
}
