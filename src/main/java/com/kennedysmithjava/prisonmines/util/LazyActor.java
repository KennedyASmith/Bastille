package com.kennedysmithjava.prisonmines.util;

import com.kennedysmithjava.prisonmines.PrisonMines;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class LazyActor {
    private static final ActorHandler HANDLER = new ActorHandler();

    private final long timeBetweenCalculation;
    private long lastCalculationTime;
    private final Runnable runnable;
    private final boolean forced;

    public LazyActor(long timeBetweenCalculation, Runnable runnable, boolean forced) {
        this.timeBetweenCalculation = timeBetweenCalculation;
        this.runnable = runnable;
        this.forced = forced;
    }

    public LazyActor(long timeBetweenCalculation, Runnable runnable) {
        this(timeBetweenCalculation, runnable, false);
    }

    public void trigger() {
        if (System.currentTimeMillis() < timeBetweenCalculation + lastCalculationTime) {
            if (this.forced) {
                HANDLER.add(this);
            }
        } else this.forceTrigger();
    }

    private boolean actorTrigger() {
        if (System.currentTimeMillis() < timeBetweenCalculation + lastCalculationTime) {
            return false;
        }

        this.forceTrigger();
        return true;
    }

    public void forceTrigger() {
        this.runnable.run();
        this.lastCalculationTime = System.currentTimeMillis();
    }

    static class ActorHandler implements Runnable {

        private final List<LazyActor> watching;

        public ActorHandler() {
            this.watching = new ArrayList<>();

            Bukkit.getScheduler().scheduleSyncRepeatingTask(PrisonMines.get(), this, 1, 1);
        }

        @Override
        public void run() {
            watching.removeIf(LazyActor::actorTrigger);
        }

        public void add(LazyActor actor) {
            this.watching.add(actor);
        }
    }
}
