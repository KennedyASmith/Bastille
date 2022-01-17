package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;

public class GuildColl extends Coll<Guild> {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final GuildColl i = new GuildColl();

    public static GuildColl get() {
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
    // OVERRIDE: COLL
    // -------------------------------------------- //

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;

    }


    @Override
    public Guild getByName(String name) {
        String compStr = MiscUtil.getComparisonString(name);
        for (Guild guild : this.getAll()) {
            if (guild.getComparisonName().equals(compStr)) {
                return guild;
            }
        }
        return null;
    }

    public Guild getByID(String UUID) {
        for (Guild guild : this.getAll()) {
            if (guild.getId().equals(UUID)) {
                return guild;
            }
        }
        return null;
    }
}
