package com.kennedysmithjava.prisoncore.cmd.type;

import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;
import org.bukkit.map.MapCursor;

public class TypeMapCursor extends TypeEnum<MapCursor.Type> {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final TypeMapCursor i = new TypeMapCursor();


    public TypeMapCursor() { super(MapCursor.Type.class); }

    public static TypeMapCursor get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String getName() {
        return "cursor";
    }


}
