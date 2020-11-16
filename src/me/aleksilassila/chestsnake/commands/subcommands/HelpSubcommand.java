package me.aleksilassila.chestsnake.commands.subcommands;

import me.aleksilassila.chestsnake.ChestSnake;
import me.aleksilassila.chestsnake.commands.Subcommand;
import me.aleksilassila.chestsnake.utils.Messages;
import org.bukkit.entity.Player;

public class HelpSubcommand extends Subcommand {
    @Override
    public void onCommand(Player player, String[] args) {
        player.sendMessage(Messages.get("info.VERSION_INFO", ChestSnake.instance.getDescription().getVersion()));
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
