package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.MineRegenCountdown;
import com.kennedysmithjava.prisonmines.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MineColl extends Coll<Mine> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MineColl i = new MineColl();

    public static MineColl get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE: COLL
    // -------------------------------------------- //



    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
    }

    @Override
    public Mine getByName(String name) {
        String compStr = MiscUtil.getComparisonString(name);
        for (Mine mine : this.getAll()) {
            if (mine.getComparisonName().equals(compStr)) {
                return mine;
            }
        }
        return null;
    }

}
