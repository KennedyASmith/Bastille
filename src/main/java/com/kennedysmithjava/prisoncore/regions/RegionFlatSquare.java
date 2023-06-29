package com.kennedysmithjava.prisoncore.regions;


import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

import java.util.Objects;

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
        this.minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        this.minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        this.maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        this.maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
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

    public int getMaxX() {
        return maxX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getMinX() {
        return minX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionFlatSquare that = (RegionFlatSquare) o;
        return minX == that.minX && minZ == that.minZ && maxX == that.maxX && maxZ == that.maxZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minZ, maxX, maxZ);
    }
}
