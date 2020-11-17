package me.aleksilassila.chestsnake.commands;

import com.sun.istack.internal.Nullable;
import me.aleksilassila.chestsnake.ChestSnake;
import me.aleksilassila.chestsnake.commands.subcommands.HelpSubcommand;
import me.aleksilassila.chestsnake.commands.subcommands.HighscoresSubcommand;
import me.aleksilassila.chestsnake.commands.subcommands.PlaySubcommand;
import me.aleksilassila.chestsnake.utils.Messages;
import me.aleksilassila.chestsnake.utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class ChestSnakeCommand implements TabExecutor {
    public final Set<Subcommand> subcommands;

    public ChestSnakeCommand() {
        ChestSnake.instance.getCommand("snake").setExecutor(this);

        subcommands = new HashSet<>();

        subcommands.add(new PlaySubcommand());
        subcommands.add(new HelpSubcommand(this));
        subcommands.add(new HighscoresSubcommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!Permissions.PLAY.hasPermission(player)) {
            player.sendMessage(Messages.get("error.NO_PERMISSION"));
            return true;
        }

        if (args.length >= 1) {
            Subcommand target = getSubcommand(args[0]);

            if (target == null) {
                player.sendMessage(Messages.get("error.SUBCOMMAND_NOT_FOUND"));
                getSubcommand("help").onCommand(player, new String[0]);
                return true;
            }

            if (target.getPermission() != null && !player.hasPermission(target.getPermission())) {
                player.sendMessage(Messages.get("error.NO_PERMISSION"));
                return true;
            }

            try {
                target.onCommand(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            } catch (Exception e) {
                player.sendMessage(Messages.get("error.ERROR"));
                return true;
            }
        }

        getSubcommand("play").onCommand(player, new String[0]);

        return true;
    }

    @Nullable
    private Subcommand getSubcommand(String name) {
        if (name.split(" ").length > 1) {
            name = name.split(" ")[1];
        }

        for (Subcommand subcommand : subcommands) {
            if (subcommand.getName().equalsIgnoreCase(name)) return subcommand;

            for (String alias : subcommand.getAliases()) {
                if (alias.equalsIgnoreCase(name)) return subcommand;
            }
        }

        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return null;

        Player player = (Player) sender;

        List<String> availableArgs = new ArrayList<>();

        if (args.length == 1) {
            for (Subcommand subcommand : subcommands) {
                if (subcommand.getPermission() == null || player.hasPermission(subcommand.getPermission()))
                    availableArgs.add(subcommand.getName());
            }
        } else if (args.length > 1) {
            Subcommand currentSubcommand = getSubcommand(args[0]);
            if (currentSubcommand == null) return null;

            if (currentSubcommand.getPermission() == null || player.hasPermission(currentSubcommand.getPermission()))
                availableArgs = currentSubcommand.onTabComplete(player, Arrays.copyOfRange(args, 1, args.length));
        }

        return availableArgs;
    }
}
