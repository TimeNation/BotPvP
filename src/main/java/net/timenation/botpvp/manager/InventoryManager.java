package net.timenation.botpvp.manager;

import net.md_5.bungee.api.ChatColor;
import net.timenation.botpvp.BotPvP;
import net.timenation.botpvp.manager.bot.BotConfig;
import net.timenation.botpvp.manager.kit.KitType;
import net.timenation.botpvp.manager.map.MapConfig;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.io.File;

public class InventoryManager {

    public void openPlayerListInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, I18n.format(player, "botpvp.inventory.invite.playerlist"));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (int i = 18; i < 27; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            inventory.addItem(new ItemManager(Material.PLAYER_HEAD, 1).setDisplayName("§8» §r" + onlinePlayer.getDisplayName()).setSkullOwner(onlinePlayer).build());
        }

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 100, 2);
    }

    public void openSelectMapInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, I18n.format(player, "botpvp.inventory.selectmap"));

        setGlassConent(inventory);

        for (File files : new File("plugins/BotPvP").listFiles()) {
            inventory.addItem(new ItemManager(Material.getMaterial(new MapConfig(files.getName().replace(".json", "")).getString("mapMaterial")), 1).setDisplayName("§8» " + TimeSpigotAPI.getInstance().getColorAPI().process(new MapConfig(files.getName().replace(".json", "")).getString("namewithcolor"))).build());
        }

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 100, 2);
    }

    public void openConfigurateBotInventory(Player player, boolean sound) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, I18n.format(player, "botpvp.inventory.bot"));
        BotConfig botConfig = BotPvP.getInstance().getGameManager().getBotConfig();

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        inventory.setItem(11, new ItemManager(Material.PLAYER_HEAD,1).setSkullOwner(botConfig.getSkin()).setDisplayName(" ").build());
        inventory.setItem(19, new ItemManager(botConfig.getWeapon().getType(), 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.weapon")).build());
        inventory.setItem(20, new ItemManager(botConfig.getChestplate().getType(), 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.chestplate")).build());
        inventory.setItem(29, new ItemManager(botConfig.getLeggings().getType(), 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.leggings")).build());
        inventory.setItem(38, new ItemManager(botConfig.getBoots().getType(), 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.boots")).build());

        inventory.setItem(16, new ItemManager(Material.STICK, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.range")).setLore(I18n.formatLines(player, "botpvp.inventory.bot.lore.full", ChatColor.of("#55f262"), botConfig.getRange())).build());
        inventory.setItem(24, new ItemManager(Material.CROSSBOW, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.kit")).setLore(" §8● " + BotPvP.getInstance().getGameManager().getKitType().getName(), "\n \n", I18n.formatLines(player, "botpvp.inventory.bot.lore.kit").get(0)).build());
        inventory.setItem(25, new ItemManager(Material.LEATHER_BOOTS, 1).setLeatherArmorColor(Color.fromRGB(65, 160, 204)).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.speed")).setLore(I18n.formatLines(player, "botpvp.inventory.bot.lore", ChatColor.of("#41a0cc"), botConfig.getSpeed())).addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES).build());
        inventory.setItem(33, new ItemManager(Material.CHAIN, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.int")).setLore(I18n.formatLines(player, "botpvp.inventory.bot.lore.full", ChatColor.of("#547aeb"), BotPvP.getInstance().getGameManager().getBots())).build());
        inventory.setItem(34, new ItemManager(Material.TOTEM_OF_UNDYING, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.lives")).setLore(I18n.formatLines(player, "botpvp.inventory.bot.lore.full", ChatColor.of("#eb4034"), botConfig.getLives())).build());
        inventory.setItem(43, new ItemManager(Material.PLAYER_HEAD, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.skin")).setSkullOwner(botConfig.getSkin()).setLore(" §8● " + ChatColor.of("#edc945") + TimeSpigotAPI.getInstance().getUuidFetcher().getName(botConfig.getSkin()), "\n \n", I18n.format(player, "botpvp.inventory.bot.lore.skin")).build());
        inventory.setItem(49, new ItemManager(Material.GLOBE_BANNER_PATTERN, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.item.template")).setLore(I18n.formatLines(player, "botpvp.inventory.bot.lore.template")).build());

        player.openInventory(inventory);
        if(sound) player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 100, 2);
    }

    public void openConfigTemplateInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, I18n.format(player, "botpvp.inventory.templates"));

        setGlassConent(inventory);

        inventory.setItem(20, new ItemManager(Material.GREEN_CONCRETE, 1).setDisplayName(I18n.format(player, "botpvp.inventory.templates.item.easy")).build());
        inventory.setItem(22, new ItemManager(Material.ORANGE_CONCRETE, 1).setDisplayName(I18n.format(player, "botpvp.inventory.templates.item.normal")).build());
        inventory.setItem(24, new ItemManager(Material.RED_CONCRETE, 1).setDisplayName(I18n.format(player, "botpvp.inventory.templates.item.hard")).build());

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 100, 2);
    }

    public void openChoosePlayerKitInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 4, I18n.format(player, "botpvp.inventory.bot.select.kit"));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (int i = 27; i < 36; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (KitType kitType : KitType.values()) {
            inventory.addItem(kitType.getItem());
        }

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 100, 2);
    }

    public void openChooseWeaponInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 4, I18n.format(player, "botpvp.inventory.bot.select.weapon"));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (int i = 27; i < 36; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        inventory.addItem(new ItemManager(Material.WOODEN_SWORD, 1).build());
        inventory.addItem(new ItemManager(Material.STONE_SWORD, 1).build());
        inventory.addItem(new ItemManager(Material.GOLDEN_SWORD, 1).build());
        inventory.addItem(new ItemManager(Material.IRON_SWORD, 1).build());
        inventory.addItem(new ItemManager(Material.DIAMOND_SWORD, 1).build());
        inventory.addItem(new ItemManager(Material.NETHERITE_SWORD, 1).build());
        inventory.setItem(18, new ItemManager(Material.WOODEN_AXE, 1).build());
        inventory.setItem(19, new ItemManager(Material.STONE_AXE, 1).build());
        inventory.setItem(20, new ItemManager(Material.GOLDEN_AXE, 1).build());
        inventory.setItem(21, new ItemManager(Material.IRON_AXE, 1).build());
        inventory.setItem(22, new ItemManager(Material.DIAMOND_AXE, 1).build());
        inventory.setItem(23, new ItemManager(Material.NETHERITE_AXE, 1).build());

        inventory.setItem(31, new ItemManager(Material.BARRIER, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.select.item.none")).build());

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 100, 2);
    }

    public void openChooseChestplateInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, I18n.format(player, "botpvp.inventory.bot.select.chestplate"));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (int i = 18; i < 27; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        inventory.addItem(new ItemManager(Material.LEATHER_CHESTPLATE, 1).build());
        inventory.addItem(new ItemManager(Material.CHAINMAIL_CHESTPLATE, 1).build());
        inventory.addItem(new ItemManager(Material.GOLDEN_CHESTPLATE, 1).build());
        inventory.addItem(new ItemManager(Material.IRON_CHESTPLATE, 1).build());
        inventory.addItem(new ItemManager(Material.DIAMOND_CHESTPLATE, 1).build());
        inventory.addItem(new ItemManager(Material.NETHERITE_CHESTPLATE, 1).build());

        inventory.setItem(22, new ItemManager(Material.BARRIER, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.select.item.none")).build());

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 100, 2);
    }

    public void openChooseLeggingsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, I18n.format(player, "botpvp.inventory.bot.select.leggings"));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (int i = 18; i < 27; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        inventory.addItem(new ItemManager(Material.LEATHER_LEGGINGS, 1).build());
        inventory.addItem(new ItemManager(Material.CHAINMAIL_LEGGINGS, 1).build());
        inventory.addItem(new ItemManager(Material.GOLDEN_LEGGINGS, 1).build());
        inventory.addItem(new ItemManager(Material.IRON_LEGGINGS, 1).build());
        inventory.addItem(new ItemManager(Material.DIAMOND_LEGGINGS, 1).build());
        inventory.addItem(new ItemManager(Material.NETHERITE_LEGGINGS, 1).build());

        inventory.setItem(22, new ItemManager(Material.BARRIER, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.select.item.none")).build());

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 100, 2);
    }

    public void openChooseBootsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, I18n.format(player, "botpvp.inventory.bot.select.boots"));

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (int i = 18; i < 27; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        inventory.addItem(new ItemManager(Material.LEATHER_BOOTS, 1).build());
        inventory.addItem(new ItemManager(Material.CHAINMAIL_BOOTS, 1).build());
        inventory.addItem(new ItemManager(Material.GOLDEN_BOOTS, 1).build());
        inventory.addItem(new ItemManager(Material.IRON_BOOTS, 1).build());
        inventory.addItem(new ItemManager(Material.DIAMOND_BOOTS, 1).build());
        inventory.addItem(new ItemManager(Material.NETHERITE_BOOTS, 1).build());

        inventory.setItem(22, new ItemManager(Material.BARRIER, 1).setDisplayName(I18n.format(player, "botpvp.inventory.bot.select.item.none")).build());

        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 100, 2);
    }

    private void setGlassConent(Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }

        for (int i = 36; i < 45; i++) {
            inventory.setItem(i, new ItemManager(Material.BLACK_STAINED_GLASS_PANE, 1).setDisplayName(" ").build());
        }
    }
}
