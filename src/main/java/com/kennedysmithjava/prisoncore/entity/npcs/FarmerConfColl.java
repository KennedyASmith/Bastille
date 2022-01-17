package com.kennedysmithjava.prisoncore.entity.npcs;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class FarmerConfColl extends Coll<FarmerConf>  {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static FarmerConfColl i = new FarmerConfColl();
    public static FarmerConfColl get() { return i; }

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
        FarmerConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
