package com.kennedysmithjava.prisoncore.quest.region;


import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

public class QuestCuboidRegion implements QuestRegion {
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public QuestCuboidRegion(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public boolean has(Location loc) {

        int locX = loc.getBlockX();
        int locY = loc.getBlockY();
        int locZ = loc.getBlockZ();

        return locX >= minX && locX <= maxX &&
                locY >= minY && locY <= maxY &&
                locZ >= minZ && locZ <= maxZ;
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
