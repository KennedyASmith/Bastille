package com.kennedysmithjava.prisoncore.util;

public enum BuildingType {

    ENCHANT_TABLE("Enchant Table"),
    BEACON("Beacon"),
    PORTAL("Portal"),
    CHEST("Storage Chest"),
    ANVIL("Anvil"),
    FURNACE("Furnace");

    final String name;
    BuildingType(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
