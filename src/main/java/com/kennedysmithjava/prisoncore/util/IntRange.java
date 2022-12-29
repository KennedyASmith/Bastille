package com.kennedysmithjava.prisoncore.util;

import java.util.Random;

public class IntRange {

    int min;
    int max;
    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getRandom(){
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }


}
