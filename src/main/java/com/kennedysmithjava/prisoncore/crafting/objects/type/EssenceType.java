package com.kennedysmithjava.prisoncore.crafting.objects.type;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum EssenceType {
    //USED FOR CRAFTING
    ANY(Material.GLOWSTONE_DUST, "&7Any Essence", new ArrayList<>()),
    AIR(Material.WHITE_DYE, "&fAir Essence", MUtil.list("&dEnchanting Material", " &r", "&7The magical essence of air!!&7")),
    FIRE(Material.BLAZE_POWDER, "&cFire Essence", MUtil.list("&dEnchanting Material", " &r", "&7The magical essence of fire!!&7")),
    WATER(Material.BLUE_DYE, "&bWater Essence", MUtil.list("&dEnchanting Material", " &r", "&7The magical essence of water!!&7")),
    EARTH(Material.BROWN_DYE, "&cEarth Essence", MUtil.list("&dEnchanting Material", " &r", "&7The magical essence of earth!!&7"));
    final String displayName;
    final List<String> lore;
    final Material material;

    EssenceType(Material material, String displayName, List<String> lore) {
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }
}
