package com.kennedysmithjava.prisoncore.quest.region;

import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

public interface QuestRegion {
    boolean has(Location location);
    boolean has(int x, int z);

    /**
     * Used for displaying this region on the quest map.
     *
     * @return true if the region can be fully displayed on the map.
     */
    boolean displayOnMap(int mapOriginX, int mapOriginZ, MapCanvas canvas);
    Location getCenterPosition();

}
