package com.kennedysmithjava.prisoncore.crafting;

public enum Rarity {
    COMMON("&fCommon", 1, 0.8),
    UNCOMMON("&aUncommon",2,  0.4),
    RARE("&9Rare", 3, 0.2),
    EPIC("&5Epic", 4, 0.01),
    LEGENDARY("&6Legendary", 5, 0.001),

    MYTHIC("&b&lMythic", 10, 0.025);


    final String displayName;

    final int rarityInt;

    final double rarityDouble;

    Rarity(String displayName, int rarity, double doubleRarity) {
        this.displayName = displayName;
        this.rarityInt = rarity;
        this.rarityDouble = doubleRarity;
    }

    public static Rarity getFromRarityInt(int rarity) {
        for (Rarity value : Rarity.values()) {
            if (value.getRarityInt() == rarity) return value;
        }
        return Rarity.COMMON;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getRarityInt() {
        return rarityInt;
    }

    public double getRarityDouble() {
        return rarityDouble;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
