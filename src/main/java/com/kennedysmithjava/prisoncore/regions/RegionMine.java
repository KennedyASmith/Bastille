package com.kennedysmithjava.prisoncore.regions;

import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

public class RegionMine implements Region {

    private final LazyRegion lazyRegion;

    public RegionMine(LazyRegion lazyRegion) {
        this.lazyRegion = lazyRegion;
    }

    @Override
    public boolean has(Location location) {
        return lazyRegion.contains(location);
    }

    @Override
    public boolean has(int x, int z) {
        return true;
    }

    @Override
    public boolean displayOnMap(int mapOriginX, int mapOriginZ, MapCanvas canvas) {
        return false;
    }

    @Override
    public Location getCenterPosition() {
        return null;
    }
}
