package net.timenation.botpvp.listener;

import net.timenation.botpvp.BotPvP;
import net.timenation.timespigotapi.manager.game.gamestates.GameState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

public class LobbyProtection implements Listener {

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE || !BotPvP.getInstance().getGameManager().getPlayerList().contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE || !BotPvP.getInstance().getGameManager().getPlayerList().contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.PHYSICAL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (BotPvP.getInstance().getGameManager().getGameState() == GameState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageEvent event) {
        if (BotPvP.getInstance().getGameManager().getGameState() == GameState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (!BotPvP.getInstance().getGameManager().getPlayerList().contains((Player) event.getDamager())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void handleFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handlePlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType().equals(Material.BOWL)) {
            event.getItemDrop().remove();
            return;
        }

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE || BotPvP.getInstance().getGameManager().getGameState() != GameState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerPickUpItem(PlayerPickupItemEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE || BotPvP.getInstance().getGameManager().getGameState() != GameState.LOBBY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handlePlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }
}
