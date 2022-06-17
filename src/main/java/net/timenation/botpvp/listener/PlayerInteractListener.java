package net.timenation.botpvp.listener;

import eu.thesimplecloud.api.CloudAPI;
import net.timenation.botpvp.BotPvP;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null || event.getItem().getItemMeta() == null || event.getItem().getType() == null || event.getItem().getItemMeta().getDisplayName() == null)
            return;

        if(event.getItem().getType() == Material.FIREWORK_ROCKET) event.setCancelled(true);

        // Items
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(BotPvP.getInstance().getGameManager().getGameState() == GameState.LOBBY) {
                if (BotPvP.getInstance().getGameManager().getGameOwner() == player.getUniqueId()) {
                    switch (event.getItem().getType()) {
                        case FIREWORK_ROCKET -> BotPvP.getInstance().getInventoryManager().openInvitePlayerInventory(player);
                        case END_CRYSTAL -> BotPvP.getInstance().getGameManager().startGame();
                        case BIG_DRIPLEAF -> BotPvP.getInstance().getInventoryManager().openSelectMapInventory(player);
                        case COMMAND_BLOCK -> BotPvP.getInstance().getInventoryManager().openConfigurateBotInventory(player, true);
                    }
                }

                if (event.getItem().getType().equals(Material.PLAYER_HEAD)) CloudAPI.getInstance().getCloudPlayerManager().getCachedCloudPlayer(player.getUniqueId()).sendToLobby();
            }
        }

        // Soups
        if (((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_AIR)) && (player.getItemInHand().getType() == Material.MUSHROOM_STEW)) {
            int health = (int) player.getHealth();
            if ((health < 20) && (health >= 13)) {
                player.setHealth(20.0D);
                player.getItemInHand().setType(Material.BOWL);
            } else if ((health < 13)) {
                player.setHealth(health + 7);
                player.getItemInHand().setType(Material.BOWL);
            }
        }
    }
}
