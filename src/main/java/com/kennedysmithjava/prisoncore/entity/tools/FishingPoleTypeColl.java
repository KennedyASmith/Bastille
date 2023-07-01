package com.kennedysmithjava.prisoncore.entity.tools;

import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;

public class FishingPoleTypeColl extends Coll<FishingPoleType>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static FishingPoleTypeColl i = new FishingPoleTypeColl();
    public static FishingPoleTypeColl get() { return i; }

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
    public FishingPoleType getByName(String name)
    {
        String compStr = MiscUtil.getComparisonString(name);
        for (FishingPoleType type : this.getAll())
        {
            if (type.getComparisonName().equals(compStr))
            {
                return type;
            }
        }
        return null;
    }

}
