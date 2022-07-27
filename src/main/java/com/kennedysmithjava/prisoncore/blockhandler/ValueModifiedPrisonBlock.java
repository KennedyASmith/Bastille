package com.kennedysmithjava.prisoncore.blockhandler;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.tools.pouch.Pouchable;
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
        base.getItemMeta().getPersistentDataContainer().set(valueKey, DataType.DOUBLE, newValue);
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
    public String getUniqueID() {
        return prisonBlock.getUniqueID();
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
