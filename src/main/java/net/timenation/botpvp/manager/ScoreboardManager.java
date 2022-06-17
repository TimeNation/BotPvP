package net.timenation.botpvp.manager;

import net.timenation.botpvp.BotPvP;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.language.I18n;
import net.timenation.timespigotapi.manager.scoreboard.ScoreboardBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardManager {

    public void sendLobbyScoreboard(Player player) {
        ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(player);

        scoreboardBuilder.setTitle(TimeSpigotAPI.getInstance().getColorAPI().process("<GRADIENT:3653bf>» BotPvP</GRADIENT:566ec4>"));
        scoreboardBuilder.setLine(0, "§8§m                            ");
        scoreboardBuilder.setLine(1, "§1");
        scoreboardBuilder.setLine(2, I18n.format(player, "botpvp.scoreboard.kit"));
        scoreboardBuilder.setLine(3, I18n.format(player, "botpvp.scoreboard.kit.info", (Object) BotPvP.getInstance().getGameManager().getKitType().getName()));
        scoreboardBuilder.setLine(4, "§2");
        scoreboardBuilder.setLine(5, I18n.format(player, "botpvp.scoreboard.players"));
        scoreboardBuilder.setLine(6, I18n.format(player, "botpvp.scoreboard.players.info", Bukkit.getOnlinePlayers().size()));
        scoreboardBuilder.setLine(7, "§3");
        scoreboardBuilder.setLine(8, I18n.format(player, "botpvp.scoreboard.map"));
        scoreboardBuilder.setLine(9, I18n.format(player, "botpvp.scoreboard.map.info", (Object) BotPvP.getInstance().getGameManager().getMapConfig().getString("namewithcolor")));
        scoreboardBuilder.setLine(10, "§4");
        scoreboardBuilder.setLine(11, "§8§m                            ");
    }

    public void sendFightScoreboard(Player player) {
        ScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(player);

        scoreboardBuilder.setTitle(TimeSpigotAPI.getInstance().getColorAPI().process("<GRADIENT:3653bf>» BotPvP</GRADIENT:566ec4>"));
        scoreboardBuilder.setLine(0, "§8§m                            ");
        scoreboardBuilder.setLine(1, "§1");
        scoreboardBuilder.setLine(2, I18n.format(player, "botpvp.scoreboard.bots"));
        scoreboardBuilder.setLine(3, I18n.format(player, "botpvp.scoreboard.bots.info", (Object) BotPvP.getInstance().getGameManager().getBotManager().getBotList().size()));
        scoreboardBuilder.setLine(4, "§2");
        scoreboardBuilder.setLine(5, I18n.format(player, "botpvp.scoreboard.living.players"));
        scoreboardBuilder.setLine(6, I18n.format(player, "botpvp.scoreboard.living.players.info", BotPvP.getInstance().getGameManager().getPlayerList().size()));
        scoreboardBuilder.setLine(7, "§3");
        scoreboardBuilder.setLine(8, I18n.format(player, "botpvp.scoreboard.map"));
        scoreboardBuilder.setLine(9, I18n.format(player, "botpvp.scoreboard.map.info", (Object) BotPvP.getInstance().getGameManager().getMapConfig().getString("namewithcolor")));
        scoreboardBuilder.setLine(10, "§4");
        scoreboardBuilder.setLine(11, "§8§m                            ");
    }
}
