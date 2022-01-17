package com.kennedysmithjava.prisoncore.entity.tools;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class PouchConfColl extends Coll<PouchConf> {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final PouchConfColl i = new PouchConfColl();
    public static PouchConfColl get() { return i; }

    @Override
    public void setActive(boolean active)
    {
        super.setActive(active);
        if (!active) return;
        PouchConf.i = this.get(MassiveCore.INSTANCE, true);
    }

}
