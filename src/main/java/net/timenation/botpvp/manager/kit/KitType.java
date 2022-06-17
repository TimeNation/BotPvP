package net.timenation.botpvp.manager.kit;

import lombok.Getter;
import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.manager.ItemManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

@Getter
public enum KitType {
    PotPvP("<SOLID:de1439>PotPvP",
            new ItemManager(Material.SPLASH_POTION).setDisplayName(TimeSpigotAPI.getInstance().getColorAPI().process("<SOLID:de1439>» PotPvP")).addPotionEffect(PotionEffectType.HEAL, 1, 1, true).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).build(),
            new ItemStack[]{ new ItemManager(Material.DIAMOND_BOOTS, 1).build(), new ItemManager(Material.DIAMOND_LEGGINGS, 1).build(), new ItemManager(Material.NETHERITE_CHESTPLATE, 1).build(), new ItemManager(Material.DIAMOND_HELMET, 1).build() },
            new ItemStack[]{ new ItemManager(Material.DIAMOND_SWORD, 1).setUnbreakable(true).addEnchantment(Enchantment.DAMAGE_ALL, 5).build() }),
    Soup("<SOLID:6e4b27>Soup",
            new ItemManager(Material.MUSHROOM_STEW).setDisplayName(TimeSpigotAPI.getInstance().getColorAPI().process("<SOLID:6e4b27>» Soup")).build(),
            new ItemStack[]{ null, null, new ItemManager(Material.IRON_CHESTPLATE, 1).build(), null },
            new ItemStack[]{ new ItemManager(Material.STONE_SWORD, 1).setUnbreakable(true).build() }),
    ShieldPvP("<SOLID:f5b942>ShieldPvP",
            new ItemManager(Material.SHIELD, 1).setDisplayName(TimeSpigotAPI.getInstance().getColorAPI().process("<SOLID:f5b942>» ShieldPvP")).build(),
            new ItemStack[]{ new ItemManager(Material.IRON_BOOTS).build(), new ItemManager(Material.IRON_LEGGINGS).build(), new ItemManager(Material.IRON_CHESTPLATE).build(), new ItemManager(Material.IRON_HELMET).build() },
            new ItemStack[]{ new ItemManager(Material.DIAMOND_SWORD, 1).addEnchantment(Enchantment.DAMAGE_ALL, 2).setUnbreakable(true).build(), new ItemManager(Material.IRON_AXE, 1).addEnchantment(Enchantment.DAMAGE_ALL, 1).setUnbreakable(true).build() });

    private final String name;
    private final ItemStack item;
    private final ItemStack[] armor;
    private final ItemStack[] inventory;

    KitType(String name, ItemStack item, ItemStack[] armor, ItemStack[] inventory) {
        this.name = name;
        this.item = item;
        this.armor = armor;
        this.inventory = inventory;
    }

    public String getName() {
        return TimeSpigotAPI.getInstance().getColorAPI().process(name);
    }
}
