package net.timenation.botpvp.listener;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.service.ServiceState;
import net.timenation.botpvp.BotPvP;
import net.timenation.botpvp.manager.bot.BotManager;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class GameListener implements Listener {

    @EventHandler
    public void handleEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        BotManager botManager = BotPvP.getInstance().getGameManager().getBotManager();

        botManager.getBotList().remove(entity);

        if (botManager.getBotList().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(player -> { BotPvP.getInstance().getScoreboardManager().sendFightScoreboard(player); });
        } else if (botManager.getBotList().size() == 0 && BotPvP.getInstance().getGameManager().getGameState() == GameState.INGAME) {
            BotPvP.getInstance().getGameManager().getPlayerList().clear();
            BotPvP.getInstance().getGameManager().setGameState(GameState.LOBBY);
            CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).setState(ServiceState.VISIBLE);
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.getInventory().clear();
                player.setHealth(20);
                player.setGameMode(GameMode.SURVIVAL);
                player.sendTitle(I18n.format(player, "botpvp.title.botkill.finish.top"), I18n.format(player, "botpvp.title.botkill.finish.bottom"));

                BotPvP.getInstance().getScoreboardManager().sendLobbyScoreboard(player);
                player.teleport(new Location(Bukkit.getWorld("world"), -756.2, 34, 210.5, -75, -3));

                if (BotPvP.getInstance().getGameManager().getGameOwner() == player.getUniqueId()) {

                    player.getInventory().setItem(2, new ItemManager(Material.FIREWORK_ROCKET, 1).setDisplayName(I18n.format(player, "botpvp.item.playerlist")).build());
                    player.getInventory().setItem(3, new ItemManager(Material.END_CRYSTAL, 1).setDisplayName(I18n.format(player, "botpvp.item.start")).build());
                    player.getInventory().setItem(5, new ItemManager(Material.BIG_DRIPLEAF, 1).setDisplayName(I18n.format(player, "botpvp.item.map")).build());
                    player.getInventory().setItem(6, new ItemManager(Material.COMMAND_BLOCK, 1).setDisplayName(I18n.format(player, "botpvp.item.configirate")).build());

                    return;
                }

                player.getInventory().setItem(4, new ItemManager(Material.PLAYER_HEAD, 1).setDisplayName(I18n.format(player, "botpvp.item.leave")).setSkullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE2Nzg3YmEzMjU2NGU3YzJmM2EwY2U2NDQ5OGVjYmIyM2I4OTg0NWU1YTY2YjVjZWM3NzM2ZjcyOWVkMzcifX19").build());
            });
        }
    }

    @EventHandler
    public void handlePlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        player.spigot().respawn();
        event.setDeathMessage(null);

        if (BotPvP.getInstance().getGameManager().getPlayerList().contains(player)) {
            BotPvP.getInstance().getGameManager().getPlayerList().remove(player);

            Bukkit.getOnlinePlayers().forEach(players -> {
                players.sendMessage(I18n.format(players, BotPvP.getInstance().getPrefix(), "botpvp.messages.death", player.getName()));
                BotPvP.getInstance().getScoreboardManager().sendFightScoreboard(players);
            });

            if (BotPvP.getInstance().getGameManager().getPlayerList().size() > 0) {
                player.getInventory().clear();
                player.setGameMode(GameMode.CREATIVE);
                player.teleport(new Location(Bukkit.getWorld(BotPvP.getInstance().getGameManager().getMapConfig().getString("world")), BotPvP.getInstance().getGameManager().getMapConfig().getDouble("player.location.x"), BotPvP.getInstance().getGameManager().getMapConfig().getDouble("player.location.y"), BotPvP.getInstance().getGameManager().getMapConfig().getDouble("player.location.z"), BotPvP.getInstance().getGameManager().getMapConfig().getFloat("player.location.yaw"), 0));
                player.getInventory().setItem(4, new ItemManager(Material.PLAYER_HEAD, 1).setDisplayName(I18n.format(player, "botpvp.item.leave")).setSkullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE2Nzg3YmEzMjU2NGU3YzJmM2EwY2U2NDQ5OGVjYmIyM2I4OTg0NWU1YTY2YjVjZWM3NzM2ZjcyOWVkMzcifX19").build());
            } else {
                BotPvP.getInstance().getGameManager().getPlayerList().clear();
                BotPvP.getInstance().getGameManager().setGameState(GameState.LOBBY);
                BotPvP.getInstance().getGameManager().getBotManager().getBotList().forEach(entity -> {
                    entity.setMetadata("player_failed", new FixedMetadataValue(BotPvP.getInstance(), ""));
                    entity.remove();
                });
                BotPvP.getInstance().getGameManager().getBotManager().getBotList().clear();
                CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).setState(ServiceState.VISIBLE);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.getInventory().clear();
                    players.setHealth(20);
                    players.setGameMode(GameMode.SURVIVAL);
                    players.sendTitle(I18n.format(players, "botpvp.title.killedbybot"), I18n.format(players, "botpvp.title.killedbybot.bottom"));

                    BotPvP.getInstance().getScoreboardManager().sendLobbyScoreboard(players);
                    players.teleport(new Location(Bukkit.getWorld("world"), 0.5, 66, 0.5));

                    if (BotPvP.getInstance().getGameManager().getGameOwner() == players.getUniqueId()) {
                        players.getInventory().setItem(2, new ItemManager(Material.FIREWORK_ROCKET, 1).setDisplayName(I18n.format(players, "botpvp.item.playerlist")).build());
                        players.getInventory().setItem(3, new ItemManager(Material.END_CRYSTAL, 1).setDisplayName(I18n.format(players, "botpvp.item.start")).build());
                        players.getInventory().setItem(5, new ItemManager(Material.BIG_DRIPLEAF, 1).setDisplayName(I18n.format(players, "botpvp.item.map")).build());
                        players.getInventory().setItem(6, new ItemManager(Material.COMMAND_BLOCK, 1).setDisplayName(I18n.format(players, "botpvp.item.configirate")).build());

                        return;
                    }

                    player.getInventory().setItem(4, new ItemManager(Material.PLAYER_HEAD, 1).setDisplayName(I18n.format(players, "botpvp.item.leave")).setSkullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE2Nzg3YmEzMjU2NGU3YzJmM2EwY2U2NDQ5OGVjYmIyM2I4OTg0NWU1YTY2YjVjZWM3NzM2ZjcyOWVkMzcifX19").build());
                });
            }
        }
    }
}
