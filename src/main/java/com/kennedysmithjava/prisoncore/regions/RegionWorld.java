package com.kennedysmithjava.prisoncore.regions;

import com.kennedysmithjava.prisoncore.engine.EngineRegions;
import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

import java.util.UUID;

public class RegionWorld implements Region {

    private UUID uuid;
    private String worldName;

    public RegionWorld(UUID uuid, String worldName) {
        this.uuid = uuid;
        this.worldName = worldName;
    }

    @Override
    public boolean has(Location location) {
        return EngineRegions.inWorld(uuid, worldName);
    }

    @Override
    public boolean has(int x, int z) {
        return EngineRegions.inWorld(uuid, worldName);
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
