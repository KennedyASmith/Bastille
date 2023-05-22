package com.kennedysmithjava.prisoncore.entity.tools;

import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;

public class PickaxeTypeColl extends Coll<PickaxeType>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static PickaxeTypeColl i = new PickaxeTypeColl();
    public static PickaxeTypeColl get() { return i; }

    // -------------------------------------------- //
    // STACK TRACEABILITY
    // -------------------------------------------- //

    @Override
    public void onTick()
    {
        super.onTick();
    }

    // -------------------------------------------- //
    // OVERRIDE: COLL
    // -------------------------------------------- //

    @Override
    public void setActive(boolean active)
    {
        super.setActive(active);
        if (!active) return;

    }


    @Override
    public PickaxeType getByName(String name)
    {
        String compStr = MiscUtil.getComparisonString(name);
        for (PickaxeType type : this.getAll())
        {
            if (type.getComparisonName().equals(compStr))
            {
                return type;
            }
        }
        return null;
    }

}
