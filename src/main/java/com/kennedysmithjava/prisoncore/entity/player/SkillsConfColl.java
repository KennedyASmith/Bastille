package com.kennedysmithjava.prisoncore.entity.player;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class SkillsConfColl extends Coll<SkillsConf> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final SkillsConfColl i = new SkillsConfColl();

    public static SkillsConfColl get() {
        return i;
    }


    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
        SkillsConf.i = this.get(MassiveCore.INSTANCE, true);
    }
}
