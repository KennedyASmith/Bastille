package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.regions.RegionType;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;
import org.jetbrains.annotations.NotNull;

public class TypeRegionType extends TypeEnum<RegionType> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final TypeRegionType i = new TypeRegionType(RegionType.class);

    public TypeRegionType(@NotNull Class<RegionType> clazz) {
        super(clazz);
    }

    public static TypeRegionType get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //


}
