package me.aleksilassila.chestsnake.utils;

import me.aleksilassila.chestsnake.ChestSnake;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.OfflinePlayer;

import java.text.MessageFormat;
import java.util.*;

public class Messages {
    private static FileConfiguration config;
    private static Map<String, String> messageCache;

    public static void init() {
        config = ChestSnake.instance.getConfig();
        Messages.messageCache = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("messages");

        for (String key : section.getKeys(false)) {
            messageCache.put(key, section.getString(key));
        }
    }

    public static void send(OfflinePlayer player, final String formatKey, final Object... objects) {
        if (player.getPlayer() != null)
            player.getPlayer().sendMessage(Messages.get(formatKey, objects));
    }

    public static String get(final String string, final Object... objects) {
        // Simple cache for messages
        if (objects.length == 0) {
            if (messageCache.containsKey(string))
                return messageCache.get(string);
            else {
                String message = getFormatString(string);
                messageCache.put(string, message);
                return message;
            }
        }
        return format(string, objects);
    }

    private static String getFormatString(final String string) {
        return messageCache.get(string);
    }

    public static String format(final String string, final Object... objects) {
        String format = getFormatString(string);
        MessageFormat messageFormat;

        try {
            messageFormat = new MessageFormat(format);
        } catch (final IllegalArgumentException e) {
            ChestSnake.instance.getLogger().severe("Invalid Translation key for '" + string + "': " + e.getMessage());
            format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
            messageFormat = new MessageFormat(format);
        }

        return messageFormat.format(objects);
    }
}