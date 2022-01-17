package com.kennedysmithjava.prisoncore.entity.mines.objects;

import org.bukkit.Material;

import java.util.List;

public class Wall {
    String schematic;
    String displayName;
    List<String> lore;
    Material icon;

    public Wall(String schematic, String displayName, List<String> lore, Material icon) {
        this.schematic = schematic;
        this.displayName = displayName;
        this.lore = lore;
        this.icon = icon;
    }

    public Material getIcon() {
        return icon;
    }

    public String getSchematic() {
        return schematic;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    @Override
    public String toString() {
        return "Wall{" +
                "schematic='" + schematic + '\'' +
                ", displayName='" + displayName + '\'' +
                ", lore=" + lore +
                ", icon=" + icon +
                '}';
    }
}
