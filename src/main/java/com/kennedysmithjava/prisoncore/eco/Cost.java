package com.kennedysmithjava.prisoncore.eco;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;

public abstract class Cost {

    /**
     * Returns true if the player meets the requirements of this cost.
     */
    public abstract boolean hasCost(MPlayer player);

    /**
     * Attempts to create a transaction with this cost on the player.
     * Returns true if successful.
     */
    public abstract boolean transaction(MPlayer player);

}
