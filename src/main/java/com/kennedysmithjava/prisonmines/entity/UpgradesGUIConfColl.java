package com.kennedysmithjava.prisonmines.entity;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class UpgradesGUIConfColl extends Coll<UpgradesGUIConf>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static UpgradesGUIConfColl i = new UpgradesGUIConfColl();
    public static UpgradesGUIConfColl get() { return i; }

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
        UpgradesGUIConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
