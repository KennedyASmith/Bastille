package com.kennedysmithjava.prisoncore.entity.mines;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class CoinCollectorGuiConfColl extends Coll<CoinCollectorGuiConf>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static CoinCollectorGuiConfColl i = new CoinCollectorGuiConfColl();
    public static CoinCollectorGuiConfColl get() { return i; }

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
        CoinCollectorGuiConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
