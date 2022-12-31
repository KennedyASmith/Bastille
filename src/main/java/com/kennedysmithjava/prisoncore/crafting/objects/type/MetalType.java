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

    // Rare
    FLINT(Material.FLINT, "&8Flint", 3, 1),
    GOLD_INGOT(Material.GOLD_INGOT, "&e&lGold Ingot", 3, 3),
    SLIME_BALL(Material.SLIME_BALL, "&aSlime Ball", 3, 1),

    // Epic
    LAPIS_LAZULI(Material.LAPIS_LAZULI, "&9Lapis Lazuli", 4, 2),
    DIAMOND(Material.DIAMOND, "&bDiamond", 4, 5),
    NETHERITE_INGOT(Material.NETHERITE_INGOT, "&8Netherite Ingot", 4, 5),

    // Legendary

    QUARTZ(Material.QUARTZ, "&f&lQuartz", 5, 5),
    EMERALD(Material.EMERALD, "&a&lEmerald", 5, 4),

    // Mythic
    HEART_OF_THE_SEA(Material.HEART_OF_THE_SEA, "&e&lHeart Of The Sea", 10, 3),
    AMETHYST(Material.AMETHYST_SHARD, "&d&lAmethyst Shard", 3, 10, Rarity.MYTHIC);
    final String displayName;
    final Material material;
    final int hardness;
    final int rarity;
    final List<String> lore;

    final Rarity rarityClass;


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
        rarityClass = Rarity.getFromRarityInt(rarity);
    }

    MetalType(Material material, String displayName, int rarity, int hardness, List<String> customLore) {
        this.displayName = displayName;
        this.hardness = hardness;
        this.rarity = rarity;
        this.material = material;
        this.lore = customLore;
        rarityClass = Rarity.getFromRarityInt(rarity);
    }

    MetalType(Material material, String displayName, int rarity, int hardness, Rarity rarityClass) {
        this.displayName = displayName;
        this.hardness = hardness;
        this.rarity = rarity;
        this.material = material;
        this.rarityClass = rarityClass;
        lore = MUtil.list(
                "&7Crafting Material",
                " &r",
                "&7Hardness: &f%hardness%",
                "&7Rarity: &f%rarity%",
                "",
                "%rarityDisplay% &7material used for crafting &etools&7!"
        );
    }

    MetalType(Material material, String displayName, int rarity, int hardness, List<String> customLore, Rarity rarityClass) {
        this.displayName = displayName;
        this.hardness = hardness;
        this.rarity = rarity;
        this.material = material;
        this.lore = customLore;
        this.rarityClass = rarityClass;
    }

}