package com.kennedysmithjava.prisoncore.util;

import java.util.concurrent.ThreadLocalRandom;

public class IntRange {

    public static ThreadLocalRandom random = ThreadLocalRandom.current();
    int min;
    int max;
    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getRandom(){
        return random.nextInt(max - min) + min;
    }


}
