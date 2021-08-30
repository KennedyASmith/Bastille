package com.kennedysmithjava.prisonmines.blockhandler;

import com.kennedysmithjava.prisonmines.entity.PrisonBlock;
import com.kennedysmithjava.prisonmines.pouch.Pouchable;
import com.mcrivals.prisoncore.eco.CurrencyType;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class ValueModifiedPrisonBlock implements Pouchable, ValueMutable {

    final PrisonBlock prisonBlock;
    double newValue;

    public ValueModifiedPrisonBlock(PrisonBlock prisonBlock, double newValue) {
        this.prisonBlock = prisonBlock;
        this.newValue = newValue;
    }

    @Override
    public ItemStack getProductItem(int amount) {
        ItemStack base = prisonBlock.getProductItem(amount);
        NBTItem item = new NBTItem(base);
        item.setDouble("VALUE", newValue);
        item.applyNBT(base);

        return base;
    }

    @Override
    public double getValue() {
        return newValue;
    }

    @Override
    public void setValue(double amount) {
        this.newValue = amount;
    }

    @Override
    public String getUniqueNbt() {
        return prisonBlock.getUniqueNbt();
    }

    @Override
    public String getDisplayName() {
        return prisonBlock.getDisplayName();
    }

    @Override
    public CurrencyType getCurrencyType() {
        return prisonBlock.getCurrencyType();
    }
}
