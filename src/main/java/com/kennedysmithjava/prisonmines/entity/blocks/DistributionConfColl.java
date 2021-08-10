package com.kennedysmithjava.prisonmines.entity.blocks;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class DistributionConfColl extends Coll<DistributionConf>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static DistributionConfColl i = new DistributionConfColl();
    public static DistributionConfColl get() { return i; }

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
        DistributionConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
