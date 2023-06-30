package com.kennedysmithjava.prisoncore.entity.mines.objects;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.blockhandler.Product;
import com.kennedysmithjava.prisoncore.blockhandler.ValueModifiedPrisonBlock;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.mines.BlocksConf;
import com.kennedysmithjava.prisoncore.pouch.Pouchable;
import com.kennedysmithjava.prisoncore.util.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PrisonBlock implements Pouchable {

    private final String name;
    private double value;
    private final Product product;
    private final BlockWrapper block;
    private final boolean isSpecial;
    private final List<String> lore;

    public PrisonBlock(String name, BlockWrapper block, Product product, double value, List<String> lore) {
        this.name = name;
        this.block = block;
        this.product = product;
        this.value = value;
        this.lore = lore;
        this.isSpecial = false;
    }

    public PrisonBlock(String name, BlockWrapper block, Product product, double value, List<String> lore, boolean isSpecial) {
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

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public ValueModifiedPrisonBlock getMutable() {
        return new ValueModifiedPrisonBlock(this, this.value);
    }

    @Override
    public String getUniqueID() {
        return String.valueOf(BlocksConf.get().getId(this));
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public CurrencyType getCurrencyType() {
        return CurrencyType.CASH;
    }

    public BlockWrapper getBlock() {
        return block;
    }

    public Product getProduct() {
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
        base.getItemMeta().getPersistentDataContainer().set(valueKey, DataType.DOUBLE, value);
        return base;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isFrom(Material material) {
        return this.product.isFrom(material);
    }


    @Override
    public String toString() {
        return "PrisonBlock{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", product=" + product +
                ", block=" + block +
                ", isSpecial=" + isSpecial +
                ", lore=" + lore +
                '}';
    }
}
