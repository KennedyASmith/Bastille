package com.kennedysmithjava.prisonmines.entity;

import org.bukkit.Material;

import java.util.List;

public class Wall {
    String schematic;
    String displayName;
    List<String> lore;
    Material icon;
    int data;

    Wall(String schematic, String displayName, List<String> lore, Material icon, int data) {
        this.schematic = schematic;
        this.displayName = displayName;
        this.lore = lore;
        this.icon = icon;
        this.data = data;
    }

    public Material getIcon() {
        return icon;
    }

    public int getData() {
        return data;
    }

    public String getSchematic() {
        return schematic;
    }

    @Override
    public String toString() {
        return "Building{" +
                "schematic='" + schematic + '\'' +
                ", icon=" + icon +
                ", data=" + data +
                '}';
    }
}
