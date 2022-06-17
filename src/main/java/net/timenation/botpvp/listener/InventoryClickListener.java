package net.timenation.botpvp.listener;

import net.timenation.botpvp.BotPvP;
import net.timenation.botpvp.manager.GameManager;
import net.timenation.botpvp.manager.bot.BotConfig;
import net.timenation.botpvp.manager.kit.KitType;
import net.timenation.botpvp.manager.map.MapConfig;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null)
            return;

        if (BotPvP.getInstance().getGameManager().getGameState() == GameState.LOBBY || !BotPvP.getInstance().getGameManager().getPlayerList().contains(player))
            event.setCancelled(true);

        if (event.getView().getTitle().equals(I18n.format(player, "botpvp.inventory.invite"))) {
            switch (event.getSlot()) {
                case 20 -> {
                    player.closeInventory();
                    player.sendMessage(I18n.format(player, "botpvp.messages.invite", BotPvP.getInstance().getPrefix()));
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 10, 2);
                    player.setMetadata("invite_friend", new FixedMetadataValue(BotPvP.getInstance(), ""));
                }
                case 24 -> BotPvP.getInstance().getInventoryManager().openPlayerListInventory(player);
            }
        }

        if (event.getView().getTitle().equals(I18n.format(player, "botpvp.inventory.selectmap"))) {
            for (File file : new File("plugins/BotPvP").listFiles()) {
                if (event.getCurrentItem().getType() == Material.valueOf(new MapConfig(file.getName().replace(".json", "")).getString("mapMaterial"))) {
                    MapConfig mapConfig = new MapConfig(file.getName().replace(".json", ""));

                    BotPvP.getInstance().getGameManager().setMapConfig(mapConfig);
                    Bukkit.getOnlinePlayers().forEach(players -> BotPvP.getInstance().getScoreboardManager().sendLobbyScoreboard(players));
                    player.sendMessage(I18n.format(player, BotPvP.getInstance().getPrefix(), "botpvp.messages.selectmap", mapConfig.getString("namewithcolor")));
                }
            }
        }

        if (event.getView().getTitle().equals(I18n.format(player, "botpvp.inventory.bot"))) {
            BotConfig botConfig = BotPvP.getInstance().getGameManager().getBotConfig();

            switch (event.getSlot()) {
                case 19 -> BotPvP.getInstance().getInventoryManager().openChooseWeaponInventory(player);
                case 20 -> BotPvP.getInstance().getInventoryManager().openChooseChestplateInventory(player);
                case 29 -> BotPvP.getInstance().getInventoryManager().openChooseLeggingsInventory(player);
                case 38 -> BotPvP.getInstance().getInventoryManager().openChooseBootsInventory(player);
                case 24 -> BotPvP.getInstance().getInventoryManager().openChoosePlayerKitInventory(player);
                case 49 -> BotPvP.getInstance().getInventoryManager().openConfigTemplateInventory(player);
                case 16 -> {
                    if (event.isLeftClick()) {
                        if (event.isShiftClick()) botConfig.setRange(botConfig.getRange() + 5);
                        else botConfig.setRange(botConfig.getRange() + 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 2);
                        BotPvP.getInstance().getInventoryManager().openConfigurateBotInventory(player, false);
                    } else if (event.isRightClick() && botConfig.getRange() > 1) {
                        botConfig.setRange(botConfig.getRange() - 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 10, 1);
                        BotPvP.getInstance().getInventoryManager().openConfigurateBotInventory(player, false);
                    }
                }
                case 25 -> {
                    if (event.isLeftClick()) {
                        botConfig.setSpeed(botConfig.getSpeed() + 0.05);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 2);
                        BotPvP.getInstance().getInventoryManager().openConfigurateBotInventory(player, false);
                    } else if (event.isRightClick() && botConfig.getSpeed() > 0.05) {
                        botConfig.setSpeed(botConfig.getSpeed() - 0.05);
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 10, 1);
                        BotPvP.getInstance().getInventoryManager().openConfigurateBotInventory(player, false);
                    }
                }
                case 33 -> {
                    if (event.isLeftClick()) {
                        if (event.isShiftClick())
                            BotPvP.getInstance().getGameManager().setBots(BotPvP.getInstance().getGameManager().getBots() + 5);
                        else
                            BotPvP.getInstance().getGameManager().setBots(BotPvP.getInstance().getGameManager().getBots() + 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 2);
                        BotPvP.getInstance().getInventoryManager().openConfigurateBotInventory(player, false);
                    } else if (event.isRightClick() && BotPvP.getInstance().getGameManager().getBots() > 1) {
                        BotPvP.getInstance().getGameManager().setBots(BotPvP.getInstance().getGameManager().getBots() - 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 10, 1);
                        BotPvP.getInstance().getInventoryManager().openConfigurateBotInventory(player, false);
                    }
                }
                case 34 -> {
                    if (event.isLeftClick()) {
                        if (event.isShiftClick()) botConfig.setLives(botConfig.getLives() + 5);
                        else botConfig.setLives(botConfig.getLives() + 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 2);
                        BotPvP.getInstance().getInventoryManager().openConfigurateBotInventory(player, false);
                    } else if (event.isRightClick() && botConfig.getLives() > 1) {
                        botConfig.setLives(botConfig.getLives() - 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 10, 1);
                        BotPvP.getInstance().getInventoryManager().openConfigurateBotInventory(player, false);
                    }
                }
                case 43 -> {
                    player.closeInventory();
                    player.sendMessage(I18n.format(player, "botpvp.messages.changeskin", BotPvP.getInstance().getPrefix()));
                    player.setMetadata("change_skin", new FixedMetadataValue(BotPvP.getInstance(), ""));
                }
            }
        }

        if (event.getView().getTitle().equals(I18n.format(player, "botpvp.inventory.bot.select.kit"))) {
            for (KitType kitType : KitType.values()) {
                if (event.getCurrentItem().equals(kitType.getItem())) {
                    BotPvP.getInstance().getGameManager().setKitType(kitType);
                    Bukkit.getOnlinePlayers().forEach(players -> BotPvP.getInstance().getScoreboardManager().sendLobbyScoreboard(players));
                    player.closeInventory();
                }
            }
        }

        if (event.getView().getTitle().equals(I18n.format(player, "botpvp.inventory.templates"))) {
            switch (event.getCurrentItem().getType()) {
                case GREEN_CONCRETE -> {
                    GameManager gameManager = BotPvP.getInstance().getGameManager();
                    BotConfig botConfig = BotPvP.getInstance().getGameManager().getBotConfig();
                    gameManager.setBots(2);
                    botConfig.setRange(5);
                    botConfig.setSpeed(0.35);
                    botConfig.setLives(60);
                    botConfig.setWeapon(new ItemManager(Material.IRON_SWORD, 1).build());

                    player.sendMessage(I18n.format(player, "botpvp.messages.succes.template", BotPvP.getInstance().getPrefix()));
                    player.closeInventory();
                }
                case ORANGE_CONCRETE -> {
                    GameManager gameManager = BotPvP.getInstance().getGameManager();
                    BotConfig botConfig = BotPvP.getInstance().getGameManager().getBotConfig();
                    gameManager.setBots(5);
                    botConfig.setRange(5);
                    botConfig.setSpeed(0.45);
                    botConfig.setLives(80);
                    botConfig.setWeapon(new ItemManager(Material.IRON_SWORD, 1).build());
                    botConfig.setChestplate(new ItemManager(Material.IRON_CHESTPLATE, 1).build());
                    botConfig.setBoots(new ItemManager(Material.IRON_BOOTS, 1).build());

                    player.sendMessage(I18n.format(player, "botpvp.messages.succes.template", BotPvP.getInstance().getPrefix()));
                    player.closeInventory();
                }
                case RED_CONCRETE -> {
                    GameManager gameManager = BotPvP.getInstance().getGameManager();
                    BotConfig botConfig = BotPvP.getInstance().getGameManager().getBotConfig();
                    gameManager.setBots(7);
                    botConfig.setRange(12);
                    botConfig.setSpeed(0.55);
                    botConfig.setLives(120);
                    botConfig.setWeapon(new ItemManager(Material.DIAMOND_SWORD, 1).build());
                    botConfig.setChestplate(new ItemManager(Material.DIAMOND_CHESTPLATE, 1).build());
                    botConfig.setLeggings(new ItemManager(Material.IRON_LEGGINGS, 1).build());
                    botConfig.setBoots(new ItemManager(Material.IRON_BOOTS, 1).build());

                    player.sendMessage(I18n.format(player, "botpvp.messages.succes.template", BotPvP.getInstance().getPrefix()));
                    player.closeInventory();
                }
            }
        }

        if (event.getView().getTitle().equals(I18n.format(player, "botpvp.inventory.bot.select.weapon"))) {
            switch (event.getCurrentItem().getType()) {
                case BARRIER, WOODEN_SWORD, STONE_SWORD, GOLDEN_SWORD, IRON_SWORD, DIAMOND_SWORD, NETHERITE_SWORD, WOODEN_AXE, STONE_AXE, GOLDEN_AXE, IRON_AXE, DIAMOND_AXE, NETHERITE_AXE -> {
                    BotPvP.getInstance().getGameManager().getBotConfig().setWeapon(event.getCurrentItem());
                    player.closeInventory();
                }
            }
        }

        if (event.getView().getTitle().equals(I18n.format(player, "botpvp.inventory.bot.select.chestplate"))) {
            switch (event.getCurrentItem().getType()) {
                case BARRIER, LEATHER_CHESTPLATE, CHAINMAIL_CHESTPLATE, GOLDEN_CHESTPLATE, IRON_CHESTPLATE, DIAMOND_CHESTPLATE, NETHERITE_CHESTPLATE -> {
                    BotPvP.getInstance().getGameManager().getBotConfig().setChestplate(event.getCurrentItem());
                    player.closeInventory();
                }
            }
        }

        if (event.getView().getTitle().equals(I18n.format(player, "botpvp.inventory.bot.select.leggings"))) {
            switch (event.getCurrentItem().getType()) {
                case BARRIER, LEATHER_LEGGINGS, CHAINMAIL_LEGGINGS, GOLDEN_LEGGINGS, IRON_LEGGINGS, DIAMOND_LEGGINGS, NETHERITE_LEGGINGS -> {
                    BotPvP.getInstance().getGameManager().getBotConfig().setLeggings(event.getCurrentItem());
                    player.closeInventory();
                }
            }
        }

        if (event.getView().getTitle().equals(I18n.format(player, "botpvp.inventory.bot.select.boots"))) {
            switch (event.getCurrentItem().getType()) {
                case BARRIER, LEATHER_BOOTS, CHAINMAIL_BOOTS, GOLDEN_BOOTS, IRON_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS -> {
                    BotPvP.getInstance().getGameManager().getBotConfig().setBoots(event.getCurrentItem());
                    player.closeInventory();
                }
            }
        }
    }
}