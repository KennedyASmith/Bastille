package com.kennedysmithjava.prisoncore.regions;

import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

import java.util.Set;

public class RegionCombined implements Region {
    private final Set<RegionFlatSquare> regions;
    public RegionCombined(Set<RegionFlatSquare> regions) {
        this.regions = regions;
    }

    public boolean has(Location loc) {
        for (RegionFlatSquare region : regions) {
            if(region.has(loc)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean has(int x, int z) {
        for (RegionFlatSquare region : regions) {
            if(region.has(x, z)){
                return true;
            }
        }
        return false;
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
