package com.kennedysmithjava.prisonmines.pouch;

import com.mcrivals.prisoncore.CurrencyType;

public class DatalessPouchable implements Pouchable {

    private final double value;
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
    public int hashCode() {
        return this.nbt.hashCode();
    }
}
