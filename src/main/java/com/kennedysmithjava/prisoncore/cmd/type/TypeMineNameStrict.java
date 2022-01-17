package com.kennedysmithjava.prisoncore.cmd.type;

public class TypeMineNameStrict extends TypeMineNameAbstract
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static TypeMineNameStrict i = new TypeMineNameStrict();
    public static TypeMineNameStrict get() {return i; }
    public TypeMineNameStrict()
    {
        super(true);
    }

}
