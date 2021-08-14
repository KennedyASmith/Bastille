package com.kennedysmithjava.prisonmines.pouch;

import com.mcrivals.prisoncore.CurrencyType;

public interface Pouchable {

    double getValue();

    String getUniqueNbt();

    String getDisplayName();

    CurrencyType getCurrencyType();

    /**
     * @return the amount that this type contributes to the pouch capacity.
     */
    default int getPouchWeight() { return 1; }

    default DatalessPouchable toDataless() {
        return new DatalessPouchable(getUniqueNbt(), getValue(), getCurrencyType(), getDisplayName());
    }
}
