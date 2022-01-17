package com.kennedysmithjava.prisoncore.entity.mines;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class CoinCollectorConfColl extends Coll<CoinCollectorConf>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static CoinCollectorConfColl i = new CoinCollectorConfColl();
    public static CoinCollectorConfColl get() { return i; }

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
        CoinCollectorConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
