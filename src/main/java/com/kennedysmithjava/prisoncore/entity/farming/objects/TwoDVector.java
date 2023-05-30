package com.kennedysmithjava.prisoncore.entity.farming.objects;

import com.massivecraft.massivecore.store.EntityInternal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TwoDVector extends EntityInternal<TwoDVector> {

    private String world;
    private int x;
    private int z;

    public void parseFromString(String n) {
        String[] array = n.split(":");
        world = array[0];
        x = Integer.parseInt(array[1]);
        z = Integer.parseInt(array[2]);
    }

    @Override
    public String toString() {
        return world + ":" + x + ":" + z;
    }

}
