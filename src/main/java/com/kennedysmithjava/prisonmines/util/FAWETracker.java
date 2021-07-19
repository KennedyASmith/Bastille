package com.kennedysmithjava.prisonmines.util;

import com.boydti.fawe.object.FaweQueue;
import com.boydti.fawe.object.RunnableVal2;

public class FAWETracker extends RunnableVal2<FaweQueue.ProgressType, Integer> {

    private boolean done;

    @Override
    public void run(FaweQueue.ProgressType value1, Integer value2) {
        if (value1.equals(FaweQueue.ProgressType.DONE))
            done = true;
    }

    public boolean isDone() {
        return done;
    }

}
