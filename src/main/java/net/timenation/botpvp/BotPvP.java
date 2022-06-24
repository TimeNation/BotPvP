package net.timenation.botpvp;

import lombok.Getter;
import net.timenation.botpvp.listener.*;
import net.timenation.botpvp.manager.GameManager;
import net.timenation.botpvp.manager.InventoryManager;
import net.timenation.botpvp.manager.ScoreboardManager;
import net.timenation.botpvp.manager.map.MapConfig;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.bot.BotListener;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Getter
public final class BotPvP extends JavaPlugin {

    private static BotPvP instance;
    private String prefix;
    private GameManager gameManager;
    private InventoryManager inventoryManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        instance = this;
        prefix = TimeSpigotAPI.getInstance().getColorAPI().process("§8» <GRADIENT:3653bf>§lBotPvP</GRADIENT:566ec4> §8- §7");
        gameManager = new GameManager();
        inventoryManager = new InventoryManager();
        scoreboardManager = new ScoreboardManager();

        List<File> maps = Arrays.stream(new File("plugins/BotPvP").listFiles()).toList();
        for (File map : maps) {
            Bukkit.createWorld(new WorldCreator(new MapConfig(map.getName().replace(".json", "")).getString("world")));
        }

        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setTime(10000);
            world.setThundering(false);
        });

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new AsyncPlayerChatListener(), this);
        pluginManager.registerEvents(new LobbyProtection(), this);
        pluginManager.registerEvents(new GameListener(), this);
        pluginManager.registerEvents(new BotListener(false), this);
    }

    public static BotPvP getInstance() {
        return instance;
    }
}
