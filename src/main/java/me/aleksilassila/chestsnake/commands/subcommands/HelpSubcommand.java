package me.aleksilassila.chestsnake.commands.subcommands;

import me.aleksilassila.chestsnake.ChestSnake;
import me.aleksilassila.chestsnake.commands.ChestSnakeCommand;
import me.aleksilassila.chestsnake.commands.Subcommand;
import me.aleksilassila.chestsnake.utils.Messages;
import org.bukkit.entity.Player;

public class HelpSubcommand extends Subcommand {
    private final ChestSnakeCommand commands;

    public HelpSubcommand(ChestSnakeCommand commands) {
        this.commands = commands;
    }

    @Override
    public void onCommand(Player player, String[] args) {
        player.sendMessage(Messages.get("info_VERSION_INFO", ChestSnake.instance.getDescription().getVersion()));

        Messages.send(player, "info_AVAILABLE_SUBCOMMANDS");

        for (Subcommand subcommand : commands.subcommands) {
            if (subcommand.getPermission() == null || player.hasPermission(subcommand.getPermission()))
                Messages.send(player, "info_AVAILABLE_SUBCOMMAND", subcommand.getName(), subcommand.help());
        }

        Messages.send(player, "info_HOW_TO_PLAY");
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String help() {
        return "Show commands and how to play";
    }

    @Override
    public String getPermission() {
        return null;
    }
}
