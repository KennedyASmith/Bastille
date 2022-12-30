package com.kennedysmithjava.prisoncore.util;

import java.util.Random;

public class IntRange {

    public static Random random = new Random();
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
