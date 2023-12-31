package com.kennedysmithjava.prisoncore.blockhandler;

import com.kennedysmithjava.prisoncore.eco.CurrencyType;

public interface Valuable {

    CurrencyType getCurrencyType();

    double getValue();


    /**
     * @return Itself if Mutable, a clone of an immutable with a mutable value or null if none exist.
     */
    ValueMutable getMutable();

}
