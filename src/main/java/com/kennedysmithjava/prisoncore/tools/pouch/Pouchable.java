package com.kennedysmithjava.prisoncore.tools.pouch;

import com.kennedysmithjava.prisoncore.blockhandler.Reward;
import com.kennedysmithjava.prisoncore.blockhandler.Valuable;

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
