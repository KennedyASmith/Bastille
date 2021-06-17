package com.kennedysmithjava.prisonmines.costs;

import com.mcrivals.prisoncore.entity.MPlayer;

public abstract class Cost {

    /**
     * Used for checking if the player can afford a transaction
     * @param player
     * @param value
     * @return returns true if the player can afford this cost.
     */
    public abstract boolean hasCost(MPlayer player);

    /**
     * Consumes the currency if able.
     * @param player
     * @param value
     * @return Returns true if the player had the Value necessary for this transaction, otherwise false.
     */
    public abstract boolean consumeCost(MPlayer player);

}
