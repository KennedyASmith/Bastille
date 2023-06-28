package com.kennedysmithjava.prisoncore.regions;

import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

import static com.kennedysmithjava.prisoncore.maps.MapUtil.insideMapBounds;

public class RegionExact implements Region {
    private final int x;
    private final int y;
    private final int z;
    private final Location centerPositon;

    public RegionExact(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.centerPositon = location;
    }

    public boolean has(Location loc) {
        return loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z;
    }

    @Override
    public boolean has(int x, int z) {
        return false;
    }

    @Override
    public boolean displayOnMap(int mapOriginX, int mapOriginZ, MapCanvas canvas) {
        int mapCenterX = 128 / 2;
        int mapCenterZ = 128 / 2;
        int mapX = mapCenterX + (x - mapOriginX);
        int mapZ = mapCenterZ + (z - mapOriginZ);
        //canvas.setPixel(mapX, mapZ, MapPalette.WHITE);
        return insideMapBounds(mapX, mapZ);
    }

    @Override
    public Location getCenterPosition() {
        return centerPositon;
    }
}
