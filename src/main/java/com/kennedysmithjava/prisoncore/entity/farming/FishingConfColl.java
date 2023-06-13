package com.kennedysmithjava.prisoncore.entity.farming;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class FishingConfColl extends Coll<FishingConf> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final FishingConfColl i = new FishingConfColl();

    public static FishingConfColl get() {
        return i;
    }

    // -------------------------------------------- //
    // STACK TRACEABILITY
    // -------------------------------------------- //

    @Override
    public void onTick() {
        super.onTick();
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        FishingConf.i = this.get(MassiveCore.INSTANCE, true);
    }
}
