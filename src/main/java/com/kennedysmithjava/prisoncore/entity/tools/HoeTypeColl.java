package com.kennedysmithjava.prisoncore.entity.tools;

import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;

public class HoeTypeColl extends Coll<HoeType>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static HoeTypeColl i = new HoeTypeColl();
    public static HoeTypeColl get() { return i; }

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
    public HoeType getByName(String name)
    {
        String compStr = MiscUtil.getComparisonString(name);
        for (HoeType type : this.getAll())
        {
            if (type.getComparisonName().equals(compStr))
            {
                return type;
            }
        }
        return null;
    }

}
