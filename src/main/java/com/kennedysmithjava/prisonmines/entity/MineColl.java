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
    public HashMap<Mine, MineRegenCountdown> countdowns = new HashMap<>();

    public static MineColl get() {
        return i;
    }

    // -------------------------------------------- //
    // STACK TRACEABILITY
    // -------------------------------------------- //

    public static HashMap<Mine, MineRegenCountdown> getCountdowns() {
        return MineColl.get().countdowns;
    }

    // -------------------------------------------- //
    // OVERRIDE: COLL
    // -------------------------------------------- //

    public static void addCountdown(Mine mine, MineRegenCountdown countdown) {
        MineColl.get().countdowns.put(mine, countdown);
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;

        // Gets all Mines, registers their timers, applies a delay from their timers to prevent parallel threads.
        Collection<Mine> coll = MineColl.get().getAll();
        coll.stream().filter(Mine::isHasTimer).forEach(mine -> {
            countdowns.put(mine, new MineRegenCountdown(mine, new Random(mine.getRegenTimer()).nextInt()));
        });
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
