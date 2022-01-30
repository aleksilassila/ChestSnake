package me.aleksilassila.chestsnake;

import me.aleksilassila.chestsnake.commands.ChestSnakeCommand;
import me.aleksilassila.chestsnake.placerholderapi.ChestSnakeExpansion;
import me.aleksilassila.chestsnake.utils.Messages;
import me.aleksilassila.chestsnake.utils.Metrics;
import me.aleksilassila.chestsnake.utils.Pair;
import me.aleksilassila.chestsnake.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class ChestSnake extends JavaPlugin {
    public static ChestSnake instance;

    private FileConfiguration highscores;
    private File highscoresFile;


    @Override
    public void onEnable() {
        instance = this;

        new UpdateChecker(85867).getVersion(version -> {
            String majorVersion = version.substring(0,version.lastIndexOf("."));
            String thisMajorVersion = this.getDescription().getVersion().substring(0, this.getDescription().getVersion().lastIndexOf("."));

            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("You are up to date.");
            } else if (majorVersion.equalsIgnoreCase(thisMajorVersion)) {
                getLogger().info("There's a new minor update available!");
            } else {
                getLogger().warning("There's a new major update available!");
            }
        });

        new Metrics(this, 9446);

        // Initialize everything
        getConfig().options().copyDefaults(true);
        saveConfig();
        initHighscoresConfig();
        Messages.init();

        new ChestSnakeCommand();

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ChestSnakeExpansion(this).register();
        }
    }

    public List<Pair<String, Integer>> getTopPlayers() {
        List<Pair<String, Integer>> topPlayers = new ArrayList<>();

        for (String uuid : getHighscoresConfig().getKeys(false)) {
            topPlayers.add(new Pair<>(uuid, getHighscoresConfig().getInt(uuid)));
        }

        topPlayers.sort(Comparator.comparingInt(Pair::getValue));
        Collections.reverse(topPlayers);

        if(topPlayers.size() > 10) {
            topPlayers = topPlayers.subList(0, 10);
        }

        return topPlayers;
    }

    public int getHighscore(OfflinePlayer player) {
        return getHighscoresConfig().getInt(player.getUniqueId().toString());
    }

    public void setHighscore(OfflinePlayer player, int score) {
        getHighscoresConfig().set(player.getUniqueId().toString(), score);
        saveHighscoresConfig();
    }

    public static FileConfiguration getHighscoresConfig() {
        return instance.highscores;
    }

    public void saveHighscoresConfig() {
        try {
            highscores.save(highscoresFile);
        } catch (IOException e) {
            getLogger().severe("Unable to save highscores");
        }
    }

    public void clearHighscoresConfig() {
        highscoresFile.delete();
        initHighscoresConfig();
    }

    private void initHighscoresConfig() {
        highscoresFile = new File(getDataFolder(), "highscores.yml");
        if (!highscoresFile.exists()) {
            highscoresFile.getParentFile().mkdirs();
            saveResource("highscores.yml", false);
        }

        highscores = new YamlConfiguration();
        try {
            highscores.load(highscoresFile);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().log(Level.SEVERE, "Unable to load highscores");
            e.printStackTrace();
        }
    }

    @Nullable
    public static OfflinePlayer getOfflinePlayer(String name) {
        return Arrays.stream(Bukkit.getOfflinePlayers()).filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
