package com.kennedysmithjava.prisoncore.entity.mines;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class BlocksConfColl extends Coll<BlocksConf>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final BlocksConfColl i = new BlocksConfColl();
    public static BlocksConfColl get() { return i; }

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
        BlocksConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
