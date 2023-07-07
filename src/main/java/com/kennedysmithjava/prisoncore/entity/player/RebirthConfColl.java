package com.kennedysmithjava.prisoncore.entity.player;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class RebirthConfColl extends Coll<RebirthConf> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final RebirthConfColl i = new RebirthConfColl();

    public static RebirthConfColl get() {
        return i;
    }


    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        RebirthConf.i = this.get(MassiveCore.INSTANCE, true);
    }
}
