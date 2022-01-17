package com.kennedysmithjava.prisoncore.blockhandler;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Product {

    Material material;

    public Product(Material material){
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getItem(int amount){
        return new ItemStack(getMaterial(), amount);
    }

    public boolean isFrom(Material material) {
        return this.material.equals(material);
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(getClass()))
            return false;

        Product blockMaterial = ((Product) obj);
        return blockMaterial.getMaterial().equals(this.getMaterial());
    }

    @Override
    public int hashCode() {
        return Objects.hash(material);
    }

    @Override
    public String toString() {
        return "Product{" +
                "material=" + material +
                '}';
    }
}
