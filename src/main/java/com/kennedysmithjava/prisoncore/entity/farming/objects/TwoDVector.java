package com.kennedysmithjava.prisoncore.entity.farming.objects;

import com.massivecraft.massivecore.store.EntityInternal;

import java.util.Objects;

public class TwoDVector extends EntityInternal<TwoDVector> {

    private final String world;
    private final int x;
    private final int z;

    public TwoDVector(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public static TwoDVector parseFromString(String n) {
        String[] array = n.split(":");
        return new TwoDVector(
                array[0],
                Integer.parseInt(array[1]),
                Integer.parseInt(array[2]));
    }

    @Override
    public String toString() {
        return world + ":" + x + ":" + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwoDVector that = (TwoDVector) o;
        return x == that.x && z == that.z && Objects.equals(world, that.world);
    }

}
