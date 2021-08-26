package com.kennedysmithjava.prisonmines.pouch;

import com.kennedysmithjava.prisonmines.blockhandler.Reward;
import com.kennedysmithjava.prisonmines.blockhandler.Valuable;
import com.kennedysmithjava.prisonmines.blockhandler.ValueMutable;

public interface Pouchable extends Reward, Valuable {

    String getUniqueNbt();

    String getDisplayName();

    /**
     * @return the amount that this type contributes to the pouch capacity.
     */
    default int getPouchWeight() { return 1; }

    default DatalessPouchable toDataless() {
        return new DatalessPouchable(getUniqueNbt(), getValue(), getCurrencyType(), getDisplayName());
    }
}
