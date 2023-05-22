package com.kennedysmithjava.prisoncore.util;

import org.bukkit.Material;

public enum RarityName {
    COMMON("§fCommon", Material.WHITE_STAINED_GLASS_PANE),
    UNCOMMON("§aUncommon", Material.LIME_STAINED_GLASS_PANE),
    RARE("§9Rare", Material.BLUE_STAINED_GLASS_PANE),
    EPIC("§5Epic", Material.PURPLE_STAINED_GLASS_PANE),
    LEGENDARY("§6Legendary", Material.ORANGE_STAINED_GLASS_PANE);

    private final String displayName;
    private final Material glassMaterial;

    RarityName(String displayName, Material glassMaterial) {
        this.displayName = displayName;
        this.glassMaterial = glassMaterial;
    }

    public String getName() {
        return displayName;
    }

    public Material getGlassMaterial() {
        return glassMaterial;
    }
}

