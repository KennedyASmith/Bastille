package com.kennedysmithjava.prisoncore.entity.mines;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class WarrenGUIConfColl extends Coll<WarrenGUIConf>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static WarrenGUIConfColl i = new WarrenGUIConfColl();
    public static WarrenGUIConfColl get() { return i; }

    // -------------------------------------------- //
    // STACK TRACEABILITY
    // -------------------------------------------- //

    @Override
    public void onTick()
    {
        super.onTick();
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void setActive(boolean active)
    {
        super.setActive(active);
        if (!active) return;
        WarrenGUIConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
