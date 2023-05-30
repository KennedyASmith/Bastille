package com.kennedysmithjava.prisoncore.quest.region;

import org.bukkit.Location;

public class QuestExactRegion implements QuestRegion {
    private final int x;
    private final int y;
    private final int z;

    public QuestExactRegion(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean has(Location loc) {
        return loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z;
    }
}
