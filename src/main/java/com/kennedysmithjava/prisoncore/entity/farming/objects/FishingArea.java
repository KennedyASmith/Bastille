package com.kennedysmithjava.prisoncore.entity.farming.objects;

import com.kennedysmithjava.prisoncore.crafting.objects.type.FishType;
import org.bukkit.Location;

import java.util.List;

@SuppressWarnings("unused")
public class FishingArea {

    public int x1;
    public int x2;

    public int z1;
    public int z2;

    public int y;

    public int levelRequired;

    List<FishType> fishObtainable;

    public FishingArea(int x1, int x2, int y, int z1, int z2, int levelRequired, List<FishType> fishObtainable) {
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
        this.levelRequired = levelRequired;
        this.fishObtainable = fishObtainable;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public List<FishType> getFishObtainable() {
        return fishObtainable;
    }

    public int getY() {
        return y;
    }

    public boolean in(Location loc){
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        return (x >= x1 && x <= x2) && (z >= z1 && z <= z2);
    }
}
