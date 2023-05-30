package com.kennedysmithjava.prisoncore.quest.region;

import org.bukkit.Location;

public class QuestCylindricalRegion implements QuestRegion {
    private final double centerX;
    private final double centerZ;
    private final double radiusSquared;
    private final double minY;
    private final double maxY;

    public QuestCylindricalRegion(double centerX, double centerZ, double radius, double minY, double maxY) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.radiusSquared = radius * radius;
        this.minY = minY;
        this.maxY = maxY;
    }

    public QuestCylindricalRegion(Location center, double radius, double height) {
        this.centerX = center.getX();
        this.centerZ = center.getZ();
        this.radiusSquared = radius * radius;
        this.minY = center.getY();
        this.maxY = center.getX() + height;
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

}
