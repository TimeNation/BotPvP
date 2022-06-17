package net.timenation.botpvp.listener;

import eu.thesimplecloud.api.CloudAPI;
import net.timenation.botpvp.BotPvP;
import net.timenation.timespigotapi.manager.ItemManager;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (BotPvP.getInstance().getGameManager().getGameOwner() == player.getUniqueId()) {
            switch (Bukkit.getOnlinePlayers().size()) {
                case 1 -> {
                    BotPvP.getInstance().getGameManager().setGameOwner(null);
                    BotPvP.getInstance().getGameManager().setDefault();
                    CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).setProperty("active_joinme", false);
                    CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).update();
                }
                default -> {
                    Player newOwner = Bukkit.getOnlinePlayers().stream().toList().get(1);
                    BotPvP.getInstance().getGameManager().setGameOwner(newOwner.getUniqueId());
                    newOwner.sendMessage(I18n.format(player, "botpvp.messages.newowner", BotPvP.getInstance().getPrefix()));
                    newOwner.playSound(newOwner.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 2, 0);

                    newOwner.getInventory().clear();
                    newOwner.getInventory().setItem(2, new ItemManager(Material.FIREWORK_ROCKET, 1).setDisplayName(I18n.format(player, "botpvp.item.invite")).build());
                    newOwner.getInventory().setItem(3, new ItemManager(Material.END_CRYSTAL, 1).setDisplayName(I18n.format(player, "botpvp.item.start")).build());
                    newOwner.getInventory().setItem(5, new ItemManager(Material.BIG_DRIPLEAF, 1).setDisplayName(I18n.format(player, "botpvp.item.map")).build());
                    newOwner.getInventory().setItem(6, new ItemManager(Material.COMMAND_BLOCK, 1).setDisplayName(I18n.format(player, "botpvp.item.configirate")).build());
                }
            }
        }
    }
}
