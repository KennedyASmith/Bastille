package com.kennedysmithjava.prisonmines.entity.blocks;

import com.kennedysmithjava.prisonmines.util.BlockMaterial;
import com.kennedysmithjava.prisonmines.util.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PrisonBlock {

    String name;
    double value;
    BlockMaterial product;
    BlockMaterial block;
    boolean isSpecial;
    List<String> lore;

    PrisonBlock(String name, BlockMaterial block, BlockMaterial product, double value, List<String> lore) {
        this.name = name;
        this.block = block;
        this.product = product;
        this.value = value;
        this.lore = lore;
        this.isSpecial = false;
    }

    PrisonBlock(String name, BlockMaterial block, BlockMaterial product, double value, List<String> lore, boolean isSpecial) {
        this.name = name;
        this.block = block;
        this.product = product;
        this.value = value;
        this.lore = lore;
        this.isSpecial = isSpecial;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public BlockMaterial getBlock() {
        return block;
    }

    public BlockMaterial getProduct() {
        return product;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemStack getProductItem(int amount) {
        ItemStack base = getProduct().getItem(amount);
        ItemMeta meta = base.getItemMeta();
        meta.setDisplayName(Color.get(getName()));
        meta.setLore(getLore());
        base.setItemMeta(meta);
        return base;
    }

    public boolean isFrom(Material material, byte data) {
        return this.product.isFrom(material, data);
    }

    @Override
    public String toString() {
        return "PrisonBlock{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", product=" + product +
                ", block=" + block +
                ", isSpecial=" + isSpecial +
                '}';
    }
}
