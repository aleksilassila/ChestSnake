package me.aleksilassila.chestsnake.commands;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Subcommand {
    public List<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }

    public abstract void onCommand(Player player, String[] args);
    public abstract String getName();
    public abstract String help();
    public abstract String getPermission();
}