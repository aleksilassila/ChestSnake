package me.aleksilassila.chestsnake.commands.subcommands;

import me.aleksilassila.chestsnake.GUIs.PlayGUI;
import me.aleksilassila.chestsnake.commands.Subcommand;
import me.aleksilassila.chestsnake.utils.Permissions;
import org.bukkit.entity.Player;

public class PlaySubcommand extends Subcommand {
    @Override
    public void onCommand(Player player, String[] args) {
        new PlayGUI(player);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String getPermission() {
        return Permissions.PLAY.getPermission();
    }
}
