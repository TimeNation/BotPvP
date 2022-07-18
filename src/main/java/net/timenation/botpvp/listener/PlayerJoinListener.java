package net.timenation.botpvp.listener;

import net.timenation.botpvp.BotPvP;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);
        player.teleport(new Location(Bukkit.getWorld("world"), -756.2, 34, 210.5, -75, -3));
        player.getInventory().clear();

        if(BotPvP.getInstance().getGameManager().getGameState() == GameState.LOBBY || BotPvP.getInstance().getGameManager().getGameState() == GameState.STARTING) {
            Bukkit.getOnlinePlayers().forEach(players -> {
                players.sendMessage(I18n.format(players, BotPvP.getInstance().getPrefix(), "botpvp.messages.join", player.getName()));
                BotPvP.getInstance().getScoreboardManager().sendLobbyScoreboard(players);
            });
        }

        if (Bukkit.getOnlinePlayers().size() == 1 && BotPvP.getInstance().getGameManager().getGameOwner() == null) {
            BotPvP.getInstance().getGameManager().setGameOwner(player.getUniqueId());
            BotPvP.getInstance().getGameManager().setGameState(GameState.LOBBY);
            BotPvP.getInstance().getGameManager().getBotConfig().setSkin(player.getUniqueId());
            BotPvP.getInstance().getInventoryManager().openConfigTemplateInventory(player);
            player.sendMessage(I18n.format(player, "botpvp.messages.join.owner", BotPvP.getInstance().getPrefix()));

            player.getInventory().setItem(2, new ItemManager(Material.FIREWORK_ROCKET, 1).setDisplayName(I18n.format(player, "botpvp.item.playerlist")).build());
            player.getInventory().setItem(3, new ItemManager(Material.END_CRYSTAL, 1).setDisplayName(I18n.format(player, "botpvp.item.start")).build());
            player.getInventory().setItem(5, new ItemManager(Material.BIG_DRIPLEAF, 1).setDisplayName(I18n.format(player, "botpvp.item.map")).build());
            player.getInventory().setItem(6, new ItemManager(Material.COMMAND_BLOCK, 1).setDisplayName(I18n.format(player, "botpvp.item.configirate")).build());

            return;
        }

        player.getInventory().clear();
        player.getInventory().setItem(4, new ItemManager(Material.PLAYER_HEAD, 1).setDisplayName(I18n.format(player, "botpvp.item.leave")).setSkullOwner("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE2Nzg3YmEzMjU2NGU3YzJmM2EwY2U2NDQ5OGVjYmIyM2I4OTg0NWU1YTY2YjVjZWM3NzM2ZjcyOWVkMzcifX19").build());
    }
}
