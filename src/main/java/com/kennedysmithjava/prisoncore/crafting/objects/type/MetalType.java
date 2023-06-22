package com.kennedysmithjava.prisoncore.crafting.objects.type;

import com.kennedysmithjava.prisoncore.crafting.Rarity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public enum MetalType {
    //Used for crafting and comparison
    ANY("&7Metal Ingot", Material.IRON_INGOT,  -1, 0, 0),
    TIN_INGOT("&6Tin Ingot", Material.IRON_INGOT, 10, 1, 1),
    COPPER_INGOT("&4Copper Ingot", Material.BRICK, 11, 1, 1),
    IRON_INGOT("&fIron Ingot", Material.NETHERITE_INGOT, 12, 1, 1),
    SILVER_INGOT("&7Silver Ingot", Material.IRON_INGOT, 13,1, 1),
    GOLD_INGOT("&eGold Ingot", Material.GOLD_INGOT, 14, 1, 1),
    ADAMANTINE_INGOT("&dAdamantine Ingot", Material.COPPER_INGOT, 15, 1, 1),
    MITHRIL_INGOT("&3Mithril Ingot", Material.IRON_INGOT, 16,1, 1),
    TITANIUM_INGOT("&8Titanium Ingot", Material.NETHERITE_INGOT, 19, 1, 1),
    PROMETHIUM_INGOT("&9Promethium Ingot", Material.IRON_INGOT, 20,1, 1),
    AETHERIUM_INGOT("&aAetherium Ingot", Material.GOLD_INGOT, 21, 1, 1),
    DRAGONSTONE_INGOT("&cDratinium Ingot", Material.NETHER_BRICK, 22, 1, 1),
    ELYSIUM_INGOT("&2Elysium Ingot", Material.NETHERITE_INGOT, 23, 1, 1),
    CELESTIUM_ORE("&bCelestium Ingot", Material.BRICK, 24, 1, 1),
    PRIMORDIAL_INGOT("&dPrimordial Ingot", Material.IRON_INGOT, 25,1, 1),
    MAGNITE_INGOT("&bMagnite Ingot", Material.COPPER_INGOT, 29, 1, 1),
    ARCANITE_INGOT("&cArcanite Ingot", Material.BRICK, 30, 1, 1),
    OSMIUM_INGOT("&8Osmium Ingot", Material.GOLD_INGOT, 31, 1, 1),
    PALLADIUM_INGOT("&8Palladium Ingot", Material.NETHER_QUARTZ_ORE, 32, 1, 1),
    VOIDITE_INGOT("&8Voidite Ingot", Material.NETHER_QUARTZ_ORE, 33, 1, 1);

    final String displayName;
    final Material material;
    final int hardness;
    final int rarity;
    final List<String> lore;

    final Rarity rarityClass;


    MetalType(String displayName, Material material, int guiSlot, int rarity, int hardness) {
        this.displayName = displayName;
        this.hardness = hardness;
        this.rarity = rarity;
        this.material = material;
        this.lore = MUtil.list(
                "&7Crafting Material",
                " &r",
                "&7Hardness: &f" + getHardness(),
                "&7Rarity: " + getRarity(),
                ""
        );
        this.rarityClass = Rarity.getFromRarityInt(rarity);
    }


    public Material getMaterial() {
        return material;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getRarity() {
        return rarity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getHardness() {
        return hardness;
    }

    public Rarity getRarityClass() {
        return rarityClass;
    }

}