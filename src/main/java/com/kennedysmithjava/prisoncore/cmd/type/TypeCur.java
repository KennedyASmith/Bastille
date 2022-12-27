package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;

public class TypeCur extends TypeEnum<CurrencyType> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static TypeCur i = new TypeCur();


    public static TypeCur get() { return i; }
    public TypeCur() { super(CurrencyType.class); }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String getName()
    {
        return "currency";
    }

    @Override
    public String getNameInner(CurrencyType value)
    {
        return value.getDisplayName();
    }
}
