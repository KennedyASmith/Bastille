package com.kennedysmithjava.prisoncore.entity.tools;

import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;

public class AxeTypeColl extends Coll<AxeType>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static AxeTypeColl i = new AxeTypeColl();
    public static AxeTypeColl get() { return i; }

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
    public AxeType getByName(String name)
    {
        String compStr = MiscUtil.getComparisonString(name);
        for (AxeType type : this.getAll())
        {
            if (type.getComparisonName().equals(compStr))
            {
                return type;
            }
        }
        return null;
    }

}
