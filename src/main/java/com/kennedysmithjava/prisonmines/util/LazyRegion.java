package com.kennedysmithjava.prisonmines.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class LazyRegion {

    private final int minX, minY, minZ, maxX, maxY, maxZ;
    private final String world;

    public LazyRegion(Location point1, Location point2) {
        if (!point1.getWorld().getName().equals(point2.getWorld().getName())) {
            throw new IllegalArgumentException("Cannot create region between multiple worlds.");
        }

        this.world = point1.getWorld().getName();

        this.minX = min(point1.getBlockX(), point2.getBlockX());
        this.maxX = max(point1.getBlockX(), point2.getBlockX());

        this.minY = min(point1.getBlockY(), point2.getBlockY());
        this.maxY = max(point1.getBlockY(), point2.getBlockY());

        this.minZ = min(point1.getBlockZ(), point2.getBlockZ());
        this.maxZ = max(point1.getBlockZ(), point2.getBlockZ());
    }

    public boolean contains(Block block) {
        return this.contains(block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
    }

    public boolean contains(Location location) {
        return this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                location.getWorld().getName());
    }

    public boolean contains(int x, int y, int z, String world) {
        return this.world.equals(world) && containsIgnoreWorld(x, y, z);
    }

    public boolean containsIgnoreWorld(int x, int y, int z) {
        return x <= this.maxX && y <= maxY && z <= maxZ
                && x >= this.minX && y >= this.minY && z >= minZ;
    }

    @Override
    public int hashCode() {
        // Since mines are in the same world, there is no point including the Mine in the HashCode
        // It will only waste time. Also, Mines are generated along the z=x line, so usually minX is
        // related to minZ, so encoding that also doesn't reduce collisions. Leaving us with the neat
        // and fast variable minX as a decent approximation for a hashcode.

        return this.minX;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public String getWorld() {
        return world;
    }
}
