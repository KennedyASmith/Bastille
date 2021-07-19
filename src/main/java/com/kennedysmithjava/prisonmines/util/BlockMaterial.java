package com.kennedysmithjava.prisonmines.util;

import org.bukkit.Material;

public class BlockMaterial {

    Material material;
    byte data;

    public BlockMaterial(Material material, byte data){
        this.material = material;
        this.data = data;
    }

    public BlockMaterial(Material material){
        this.material = material;
        this.data = (byte) 0;
    }

    public byte getData() {
        return data;
    }

    public Material getMaterial() {
        return material;
    }
}
