package com.kennedysmithjava.prisoncore.entity.npcs;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class FarmerGuiConfColl extends Coll<FarmerGuiConf> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static FarmerGuiConfColl i = new FarmerGuiConfColl();

    public static FarmerGuiConfColl get() {
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
        FarmerGuiConf.i = this.get(MassiveCore.INSTANCE, true);
    }
}
