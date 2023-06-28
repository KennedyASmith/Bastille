package com.kennedysmithjava.prisoncore.regions;


import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

public class RegionFlatSquare implements Region {
    private final int minX;
    private final int minZ;
    private final int maxX;
    private final int maxZ;

    public RegionFlatSquare(int minX, int minZ, int maxX, int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public RegionFlatSquare(Location pos1, Location pos2) {
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        this.minX = Math.min(minX, maxX);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public boolean has(Location loc) {
        int locX = loc.getBlockX();
        int locZ = loc.getBlockZ();
        return has(locX, locZ);
    }

    @Override
    public boolean has(int x, int z) {
        return x >= minX && x <= maxX &&
                z >= minZ && z <= maxZ;
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
