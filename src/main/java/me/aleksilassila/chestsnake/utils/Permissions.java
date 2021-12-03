package me.aleksilassila.chestsnake.utils;

import org.bukkit.entity.Player;

public enum Permissions {
    VIEW_HIGHSCORES("chestsnake.highscores.view"),
    PLAY("chestsnake.play");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission(Player player) {
        return player.hasPermission(getPermission());
    }
}
