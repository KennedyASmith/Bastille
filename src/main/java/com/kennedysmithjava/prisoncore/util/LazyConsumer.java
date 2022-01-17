package com.kennedysmithjava.prisoncore.util;

import com.kennedysmithjava.prisoncore.PrisonCore;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LazyConsumer<T> {
    private static final ActorHandler HANDLER = new ActorHandler();

    private final long timeBetweenCalculation;
    private long lastCalculationTime = 0L;
    private final Consumer<T> consumer;
    private final boolean forced;

    public LazyConsumer(long timeBetweenCalculation, Consumer<T> consumer, boolean forced) {
        this.timeBetweenCalculation = timeBetweenCalculation;
        this.consumer = consumer;
        this.forced = forced;
    }

    public LazyConsumer(long timeBetweenCalculation, Consumer<T> consumer) {
        this(timeBetweenCalculation, consumer, false);
    }

    public void trigger(T t) {
        if (System.currentTimeMillis() < timeBetweenCalculation + lastCalculationTime) {
            if (this.forced) {
                HANDLER.add(this, t);
            }
        } else this.forceTrigger(t);
    }

    @SuppressWarnings("unchecked")
    private boolean actorTrigger(Object t) {
        if (System.currentTimeMillis() < timeBetweenCalculation + lastCalculationTime) {
            return false;
        }

        this.forceTrigger((T) t);
        return true;
    }

    public void forceTrigger(T t) {
        this.consumer.accept(t);
        this.lastCalculationTime = System.currentTimeMillis();
    }

    static class ActorHandler implements Runnable {

        private final Map<LazyConsumer<?>, Object> watching;

        public ActorHandler() {
            this.watching = new HashMap<>();

            Bukkit.getScheduler().scheduleSyncRepeatingTask(PrisonCore.get(), this, 1, 1);
        }

        @Override
        public void run() {
            watching.entrySet().removeIf(e -> e.getKey().actorTrigger(e.getValue()));
        }

        public <X> void add(LazyConsumer<X> actor, X x) {
            this.watching.put(actor, x);
        }
    }

}
