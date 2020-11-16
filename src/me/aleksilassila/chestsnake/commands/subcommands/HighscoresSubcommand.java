package me.aleksilassila.chestsnake.commands.subcommands;

import javafx.util.Pair;
import me.aleksilassila.chestsnake.ChestSnake;
import me.aleksilassila.chestsnake.commands.Subcommand;
import me.aleksilassila.chestsnake.utils.Messages;
import me.aleksilassila.chestsnake.utils.Permissions;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HighscoresSubcommand extends Subcommand {
    @Override
    public void onCommand(Player player, String[] args) {
        Messages.send(player, "HIGHSCORES");

        int index = 1;
        for (Pair<String, Integer> score : ChestSnake.instance.getTopPlayers()) {
            try {
                UUID uuid = UUID.fromString(score.getKey());

                Messages.send(player,
                        "HIGHSCORE_ITEM",
                        index,
                        ChestSnake.instance.getServer().getOfflinePlayer(uuid).getName(),
                        score.getValue());
                index++;
            } catch (Exception ignored) {}
        }
    }

    @Override
    public String getName() {
        return "highscores";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"scoreboard", "top"};
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String getPermission() {
        return Permissions.VIEW_HIGHSCORES.getPermission();
    }
}
