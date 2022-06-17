package net.timenation.botpvp.manager.bot;

import lombok.Getter;
import lombok.Setter;
import net.timenation.timespigotapi.manager.ItemManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@Setter
public class BotConfig {

    private String name;
    private UUID skin;
    private double range;
    private double speed;
    private int lives;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack weapon;

    public BotConfig() {
        this.name = "TimeBot";
        this.skin = UUID.fromString("c1685728-72d6-4dbe-8899-28c4aa3cb93c");
        this.range = 3.0;
        this.speed = 0.35;
        this.lives = 20;
        this.helmet = null;
        this.chestplate = null;
        this.leggings = null;
        this.boots = null;
        this.weapon = new ItemManager(Material.STONE_SWORD, 1).build();
        this.chestplate = new ItemManager(Material.BARRIER, 1).build();
        this.leggings = new ItemManager(Material.BARRIER, 1).build();
        this.boots = new ItemManager(Material.BARRIER, 1).build();
    }
}
