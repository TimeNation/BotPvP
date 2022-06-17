package net.timenation.botpvp.manager;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.service.ServiceState;
import lombok.Getter;
import lombok.Setter;
import net.timenation.botpvp.BotPvP;
import net.timenation.botpvp.manager.bot.BotConfig;
import net.timenation.botpvp.manager.bot.BotManager;
import net.timenation.botpvp.manager.kit.KitType;
import net.timenation.botpvp.manager.map.MapConfig;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.bot.PvPBot;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class GameManager {

    private UUID gameOwner;
    private KitType kitType;
    private BotManager botManager;
    private BotConfig botConfig;
    private MapConfig mapConfig;
    private GameState gameState;
    private List<Player> playerList;
    private boolean ingame;
    private int bots;

    public GameManager() {
        this.gameOwner = null;
        this.kitType = KitType.PotPvP;
        this.botManager = new BotManager();
        this.botConfig = new BotConfig();
        this.mapConfig = new MapConfig("starwars");
        this.gameState = GameState.STARTING;
        this.playerList = new ArrayList<>();
        this.ingame = false;
        this.bots = 1;
    }

    public void setDefault() {
        this.gameOwner = null;
        this.kitType = KitType.PotPvP;
        this.botConfig = new BotConfig();
        this.mapConfig = new MapConfig("starwars");
        this.gameState = GameState.STARTING;
        this.ingame = false;
        this.bots = 1;
    }

    public void startGame() {
        AtomicInteger countdown = new AtomicInteger(    5);
        this.gameState = GameState.INGAME;

        CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).setState(ServiceState.INVISIBLE);

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.teleport(new Location(Bukkit.getWorld(this.mapConfig.getString("world")), this.mapConfig.getDouble("player.location.x"), this.mapConfig.getDouble("player.location.y"), this.mapConfig.getDouble("player.location.z"), this.mapConfig.getFloat("player.location.yaw"), 0));
            player.getInventory().clear();
            player.getInventory().setArmorContents(this.kitType.getArmor());
            for (ItemStack itemStack : this.kitType.getInventory()) {
                player.getInventory().addItem(itemStack);
            }

            this.playerList.add(player);
            BotPvP.getInstance().getScoreboardManager().sendFightScoreboard(player);

            switch (this.kitType) {
                case PotPvP -> {
                    for (int i = 0; i < 35; i++) {
                        player.getInventory().addItem(new ItemManager(Material.SPLASH_POTION).addPotionEffect(PotionEffectType.HEAL, 2, 1, true).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).build());
                    }
                }
                case Soup -> {
                    for (int i = 0; i < 35; i++) {
                        player.getInventory().addItem(new ItemManager(Material.MUSHROOM_STEW).build());
                    }

                    player.getInventory().setItem(14, new ItemManager(Material.BOWL, 64).build());
                    player.getInventory().setItem(15, new ItemManager(Material.BROWN_MUSHROOM, 64).build());
                    player.getInventory().setItem(16, new ItemManager(Material.RED_MUSHROOM, 64).build());
                }
                case ShieldPvP -> {
                    player.getInventory().setItemInOffHand(new ItemManager(Material.SHIELD, 1).setUnbreakable(true).build());
                    player.getInventory().setItem(8, new ItemManager(Material.GOLDEN_APPLE, 4).build());
                }
            }
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                switch (countdown.get()) {
                    case 5, 4, 3, 2, 1 -> {
                        Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(TimeSpigotAPI.getInstance().getColorAPI().process("<SOLID:5cff5c>" + countdown.get()), I18n.format(player, "botpvp.title.countdown", countdown.get())));
                        countdown.getAndDecrement();
                    }
                    case 0 -> {
                        Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(TimeSpigotAPI.getInstance().getColorAPI().process("<SOLID:5cff5c>" + countdown.get()), I18n.format(player, "botpvp.title.start", countdown.get())));

                        Bukkit.getScheduler().runTask(BotPvP.getInstance(), () -> {
                            for (int i = 0; i < BotPvP.getInstance().getGameManager().getBots(); i++) {
                                PvPBot pvpBot = new PvPBot(Bukkit.getWorld(BotPvP.getInstance().getGameManager().getMapConfig().getString("world")),
                                        new Location(Bukkit.getWorld(BotPvP.getInstance().getGameManager().getMapConfig().getString("world")),
                                                BotPvP.getInstance().getGameManager().getMapConfig().getDouble("bot.location.x"),
                                                BotPvP.getInstance().getGameManager().getMapConfig().getDouble("bot.location.y"),
                                                BotPvP.getInstance().getGameManager().getMapConfig().getDouble("bot.location.z"),
                                                BotPvP.getInstance().getGameManager().getMapConfig().getFloat("bot.location.yaw"), 0),
                                        BotPvP.getInstance().getGameManager().getBotConfig().getName(),
                                        Bukkit.getOnlinePlayers().stream().toList().get(0),
                                        BotPvP.getInstance());
                                pvpBot.withRange(BotPvP.getInstance().getGameManager().getBotConfig().getRange());
                                pvpBot.withMovementSpeed(BotPvP.getInstance().getGameManager().getBotConfig().getSpeed());
                                pvpBot.withHealth(BotPvP.getInstance().getGameManager().getBotConfig().getLives());
                                pvpBot.withFollowRange(200);
                                pvpBot.withSkin(TimeSpigotAPI.getInstance().getUuidFetcher().getName(BotPvP.getInstance().getGameManager().getBotConfig().getSkin()));
                                pvpBot.withItemInSlot(EquipmentSlot.HAND, BotPvP.getInstance().getGameManager().getBotConfig().getWeapon());
                                pvpBot.withItemInSlot(EquipmentSlot.CHEST, BotPvP.getInstance().getGameManager().getBotConfig().getChestplate());
                                pvpBot.withItemInSlot(EquipmentSlot.LEGS, BotPvP.getInstance().getGameManager().getBotConfig().getLeggings());
                                pvpBot.withItemInSlot(EquipmentSlot.FEET, BotPvP.getInstance().getGameManager().getBotConfig().getBoots());
                                pvpBot.spawn(new Location(Bukkit.getWorld(BotPvP.getInstance().getGameManager().getMapConfig().getString("world")),
                                        BotPvP.getInstance().getGameManager().getMapConfig().getDouble("bot.location.x"),
                                        BotPvP.getInstance().getGameManager().getMapConfig().getDouble("bot.location.y"),
                                        BotPvP.getInstance().getGameManager().getMapConfig().getDouble("bot.location.z"),
                                        BotPvP.getInstance().getGameManager().getMapConfig().getFloat("bot.location.yaw"), 0));
                                BotPvP.getInstance().getGameManager().getBotManager().getBotList().add(pvpBot.getEntity());
                                Bukkit.getOnlinePlayers().forEach(player -> BotPvP.getInstance().getScoreboardManager().sendFightScoreboard(player));
                            }
                        });

                        cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(BotPvP.getInstance(), 0, 20);
    }
}
