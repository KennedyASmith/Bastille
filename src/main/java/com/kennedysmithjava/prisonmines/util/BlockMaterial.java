package com.kennedysmithjava.prisonmines.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class BlockMaterial {

    Material material;
    byte data;

    public BlockMaterial(Material material, byte data){
        this.material = material;
        this.data = data;
    }

    public BlockMaterial(Material material, int data){
        this.material = material;
        this.data = (byte) data;
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

    public ItemStack getItem(int amount){
        return new ItemStack(getMaterial(), 1, (short) 0, getData());
    }

    public boolean isFrom(Material material, byte data) {
        return this.material.equals(material) && this.data == data;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(getClass()))
            return false;

        BlockMaterial blockMaterial = ((BlockMaterial) obj);
        return blockMaterial.getMaterial().equals(this.getMaterial()) && this.data == blockMaterial.getData();
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, data);
    }
}
