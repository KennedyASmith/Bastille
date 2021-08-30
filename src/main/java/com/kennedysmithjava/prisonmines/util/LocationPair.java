package com.kennedysmithjava.prisonmines.util;


import org.bukkit.Location;

public class LocationPair {

    Location min;
    Location max;

    public LocationPair(Location min, Location max) {
        this.min = min;
        this.max = max;
    }

    public Location getMax() {
        return max;
    }

    public Location getMin() {
        return min;
    }
}
