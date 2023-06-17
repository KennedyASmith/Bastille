package com.kennedysmithjava.prisoncore.quest.region;

import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

import java.util.Set;

public class QuestCombinedRegion implements QuestRegion {
    private final Set<QuestRegion> regions;
    public QuestCombinedRegion(Set<QuestRegion> regions) {
        this.regions = regions;
    }

    public boolean has(Location loc) {
        for (QuestRegion region : regions) {
            if(region.has(loc)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean has(int x, int z) {
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
