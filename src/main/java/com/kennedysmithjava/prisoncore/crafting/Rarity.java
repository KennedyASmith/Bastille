package com.kennedysmithjava.prisoncore.crafting;

import lombok.Getter;

public enum Rarity {
    COMMON("&fCommon", 1),
    UNCOMMON("&aUnCommon", 2),
    RARE("&9Rare", 3),
    EPIC("&5Epic", 4),
    LEGENDARY("&6Legendary", 5),

    MYTHIC("&b&lMYTHIC", 10);


    final String displayName;
    @Getter
    final int rarity;

    Rarity(String displayName, int rarity) {
        this.displayName = displayName;
        this.rarity = rarity;
    }

    public static Rarity getFromRarityInt(int rarity) {
        for (Rarity value : Rarity.values()) {
            if (value.getRarity() == rarity) return value;
        }
        return Rarity.COMMON;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
