package com.kennedysmithjava.prisoncore.regions;

public class Scaling {

    /**
     * Scales 0 to 1 given 0 to max (inclusive)
     * @param x level
     * @param max max Level
     * @return
     */
    public static double log(int x, int max) {
        return Math.log(x + 1) / Math.log(max + 1);
    }

    /**
     * Scales 0 to 1 given 0 to max (inclusive)
     * @param x level
     * @param max max Level
     * @return
     */
    public static double squared(int x, int max) {
        return (double)(x * x) / (double)(max * max); //Scales 0 to 1 given 0 to MAX_POWER (inclusive)
    }

    /**
     * Scales 0 to 1 given 0 to max (inclusive)
     * @param x level
     * @param max max Level
     * @return
     */
    public static double exp(int x, int max) {
        return Math.exp(x) / Math.exp(max); //Scales 0 to 1 given 0 to MAX_POWER (inclusive)
    }

    /**
     * Scales 0 to 1 given 0 to max (inclusive)
     * @param x level
     * @param max max Level
     * @return
     */
    public static double linear(int x, int max) {
        return (double)x / max;
    }

}
