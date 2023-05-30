package com.kennedysmithjava.prisoncore.quest.region;

import org.bukkit.Location;

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
}
