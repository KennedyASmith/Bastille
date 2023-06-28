package com.kennedysmithjava.prisoncore.regions;

import org.bukkit.Location;
import org.bukkit.map.MapCanvas;

import static com.kennedysmithjava.prisoncore.maps.MapUtil.insideMapBounds;

public class RegionCylindrical implements Region {
    private final double centerX;
    private final double centerZ;
    private final double radiusSquared;
    private final double minY;
    private final double maxY;

    private final Location centerPosition;

    public RegionCylindrical(Location center, double radius, double height) {
        this.centerX = center.getX();
        this.centerZ = center.getZ();
        this.radiusSquared = radius * radius;
        this.minY = center.getY();
        this.maxY = center.getX() + height;
        this.centerPosition = center;
    }

    public boolean has(Location loc) {
        double locX = loc.getX();
        double locZ = loc.getZ();
        double locY = loc.getY();

        double dx = locX - centerX;
        double dz = locZ - centerZ;

        double distanceSquared = dx * dx + dz * dz;

        return distanceSquared <= radiusSquared && locY >= minY && locY <= maxY;
    }

    @Override
    public boolean has(int x, int z) {
        double dx = x - centerX;
        double dz = z - centerZ;

        double distanceSquared = dx * dx + dz * dz;

        return distanceSquared <= radiusSquared;
    }


    @Override
    public boolean displayOnMap(int mapOriginX, int mapOriginZ, MapCanvas canvas) {
        int mapCenterX = 128 / 2;
        int mapCenterZ = 128 / 2;
        int mapX = mapCenterX + ((int) centerX - mapOriginX);
        int mapZ = mapCenterZ + ((int) centerZ - mapOriginZ);
        return insideMapBounds(mapX, mapZ);
    }

    public boolean liesOnCircumference(double x, double z) {
        double dx = x - centerX;
        double dz = z - centerZ;

        double distanceSquared = dx * dx + dz * dz;

        return Math.abs(distanceSquared - radiusSquared) < 1.0; // Adjust the threshold as needed
    }

    @Override
    public Location getCenterPosition() {
        return centerPosition;
    }

}
