package net.timenation.botpvp.manager.bot;

import lombok.Getter;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BotManager {

    private final List<Entity> botList;

    public BotManager() {
        botList = new ArrayList<>();
    }
}
