package com.kennedysmithjava.prisoncore.crafting;

public enum Rarity {
    COMMON("&fCommon"),
    UNCOMMON("&aUnCommon"),
    RARE("&9Rare"),
    EPIC("&5Epic"),
    LEGENDARY("&6Legendary"),

    MYTHIC("&b&lMYTHIC");


    final String displayName;

    Rarity(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
