package com.kennedysmithjava.prisoncore.pouch;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.blockhandler.Reward;
import com.kennedysmithjava.prisoncore.blockhandler.Valuable;
import org.bukkit.NamespacedKey;

public interface Pouchable extends Reward, Valuable {

    NamespacedKey valueKey = new NamespacedKey(PrisonCore.get(), "pVALUE");

    String getUniqueID();

    String getDisplayName();

    /**
     * @return the amount that this type contributes to the pouch capacity.
     */
    default int getPouchWeight() { return 1; }

    default DatalessPouchable toDataless() {
        return new DatalessPouchable(getUniqueID(), getValue(), getCurrencyType(), getDisplayName());
    }
}
