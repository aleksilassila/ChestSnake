package me.aleksilassila.chestsnake.placerholderapi;

import me.aleksilassila.chestsnake.ChestSnake;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class ChestSnakeExpansion extends PlaceholderExpansion {
    private final ChestSnake plugin;

    public ChestSnakeExpansion(ChestSnake plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "chestsnake";
    }

    @Override
    public @NotNull String getAuthor() {
        return "aleksilassila";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(params.equalsIgnoreCase("highscore")) {
            return player == null ? "" : String.valueOf(plugin.getHighscore(player));
        }
        if(params.startsWith("highscore_")) {
            String[] args = params.split("_");
            if(args.length == 2) {
                return ChestSnake.getOfflinePlayer(args[1]) == null ? "" : String.valueOf(plugin.getHighscore(ChestSnake.getOfflinePlayer(args[1])));
            }
        }
        return null;
    }
}
