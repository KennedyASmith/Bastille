package com.kennedysmithjava.prisoncore.crafting.objects.type;

import com.kennedysmithjava.prisoncore.crafting.Rarity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public enum MetalType {

    // Common
    IRON_INGOT(Material.IRON_INGOT, "&7Iron Ingot", 1, 4),
    COAL(Material.COAL, "&0Coal", 1, 1),

    // Uncommon
    COPPER(Material.COPPER_INGOT, "&6Copper", 2, 3),
    BRICK(Material.BRICK, "&6Brick", 2, 2),
    SCUTE(Material.SCUTE, "&aScute", 2, 4),
    CLAY_BALL(Material.CLAY_BALL, "&7Clay Ball", 2, 2),

    FLINT(Material.FLINT, "&8Flint", 3, 1);

    final String displayName;
    final Material material;
    final int hardness;
    final int rarity;
    final List<String> lore;


    MetalType(Material material, String displayName, int rarity, int hardness) {
        this.displayName = displayName;
        this.hardness = hardness;
        this.rarity = rarity;
        this.material = material;

        lore = MUtil.list(
                "&7Crafting Material",
                " &r",
                "&7Hardness: &f%hardness%",
                "&7Rarity: &f%rarity%",
                "",
                "%rarityDisplay% &7material used for crafting &etools&7!"
        );
    }

    MetalType(Material material, String displayName, int rarity, int hardness, List<String> customLore) {
        this.displayName = displayName;
        this.hardness = hardness;
        this.rarity = rarity;
        this.material = material;
        this.lore = customLore;
    }

}