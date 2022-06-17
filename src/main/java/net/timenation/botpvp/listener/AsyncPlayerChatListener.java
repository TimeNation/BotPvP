package net.timenation.botpvp.listener;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import eu.thesimplecloud.api.player.text.CloudText;
import net.timenation.botpvp.BotPvP;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.language.I18n;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void handleAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("invite_friend")) {
            ICloudPlayer iCloudPlayer = CloudAPI.getInstance().getCloudPlayerManager().getCloudPlayer(event.getMessage()).getBlockingOrNull();

            if (iCloudPlayer == null) {
                player.sendMessage(I18n.format(player, "botpvp.messages.invite.error", BotPvP.getInstance().getPrefix()));
                player.removeMetadata("invite_friend", BotPvP.getInstance());

                event.setCancelled(true);
                return;
            }

            // TODO: Code PacketPlayOutPlayerInvite packet with rabbitmq!
            iCloudPlayer.sendMessage(BotPvP.getInstance().getPrefix() + "§7Du wurdest zu einer Runde " + TimeSpigotAPI.getInstance().getColorAPI().process("<GRADIENT:3653bf>BotPvP</GRADIENT:566ec4>") + "  §7von §d" + player.getName() + " §7eingeladen§8.");
            CloudText cloudText = new CloudText("§7Klicke §a§lHIER§8, §7damit du beitreten kannst§8! §7:)");
            cloudText.addClickEvent(CloudText.ClickEventType.RUN_COMMAND, "/join " + CloudAPI.getInstance().getThisSidesName());
            iCloudPlayer.sendMessage(cloudText);

            CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).setProperty("active_joinme", true);
            CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(CloudAPI.getInstance().getThisSidesName()).update();

            player.sendMessage(I18n.format(player, "botpvp.messages.invite.successfully", BotPvP.getInstance().getPrefix()));
            player.removeMetadata("invite_friend", BotPvP.getInstance());
            event.setCancelled(true);
        } else if (player.hasMetadata("change_skin")) {
            player.sendMessage(I18n.format(player, "botpvp.messages.changeskin.loading", BotPvP.getInstance().getPrefix()));

            UUID uuid = TimeSpigotAPI.getInstance().getUuidFetcher().getUUID(event.getMessage());

            if(uuid == null) {
                player.sendMessage(I18n.format(player, "botpvp.messages.changeskin.error", BotPvP.getInstance().getPrefix()));
                player.removeMetadata("change_skin", BotPvP.getInstance());

                event.setCancelled(true);
                return;
            }

            BotPvP.getInstance().getGameManager().getBotConfig().setSkin(uuid);
            player.sendMessage(I18n.format(player, "botpvp.messages.changeskin.successfully", BotPvP.getInstance().getPrefix()));
            player.removeMetadata("change_skin", BotPvP.getInstance());
            event.setCancelled(true);
        }
    }
}
