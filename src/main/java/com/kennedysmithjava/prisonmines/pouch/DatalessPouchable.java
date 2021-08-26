package com.kennedysmithjava.prisonmines.pouch;

import com.kennedysmithjava.prisonmines.blockhandler.ValueMutable;
import com.mcrivals.prisoncore.CurrencyType;
import org.bukkit.inventory.ItemStack;

public class DatalessPouchable implements Pouchable, ValueMutable, Comparable<DatalessPouchable> {

    private double value;
    private final String nbt;
    private final String displayName;
    private final CurrencyType currencyType;

    public DatalessPouchable(String nbt, double value, CurrencyType currencyType, String displayName) {
        this.value = value;
        this.nbt = nbt;
        this.displayName = displayName;
        this.currencyType = currencyType;
    }

    @Override
    public double getValue() {
        return this.value;
    }

    @Override
    public void setValue(double amount) {
        this.value = amount;
    }

    @Override
    public String getUniqueNbt() {
        return this.nbt;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    @Override
    public String toString() {
        return "DatalessPouchable{" +
                "value=" + value +
                ", nbt='" + nbt + '\'' +
                ", displayName='" + displayName + '\'' +
                ", currencyType=" + currencyType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatalessPouchable pouchable = (DatalessPouchable) o;
        return nbt.equals(pouchable.nbt);
    }

    @Override
    public int hashCode() {
        return this.nbt.hashCode();
    }

    @Override
    public int compareTo(DatalessPouchable o) {
        return nbt.compareTo(o.getUniqueNbt());
    }

    @Override
    public ItemStack getProductItem(int amount) {
        return null;
    }
}
