package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.eco.CurrencyType;

public class TypeCurrency extends TypeCur {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final TypeCurrency i = new TypeCurrency();

    public TypeCurrency() {
        this.setAll(CurrencyType.values());
    }

    public static TypeCurrency get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String getName() {
        return "currencies";
    }


}
