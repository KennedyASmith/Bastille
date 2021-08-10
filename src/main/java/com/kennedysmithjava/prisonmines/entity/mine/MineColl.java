package com.kennedysmithjava.prisonmines.entity.mine;

import com.kennedysmithjava.prisonmines.MinesWorldManager;
import com.kennedysmithjava.prisonmines.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.block.Block;

public class MineColl extends Coll<Mine> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MineColl i = new MineColl();

    public static MineColl get() {
        return i;
    }

    private final Int2ObjectMap<Mine> mineLocationCache;

    public MineColl() {
        this.mineLocationCache = new Int2ObjectArrayMap<>();
    }

    // -------------------------------------------- //
    // OVERRIDE: COLL
    // -------------------------------------------- //

    @Override
    public void postAttach(Mine entity, String id) {
        super.postAttach(entity, id);

        this.mineLocationCache.put(this.getLocationHashKey(entity), entity);
    }

    @Override
    public void onTick() {
        super.onTick();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        // if (!active) return;
    }

    @Override
    public Mine getByName(String name) {
        String compStr = MiscUtil.getComparisonString(name);
        for (Mine mine : this.getAll()) {
            if (mine.getComparisonName().equals(compStr)) {
                return mine;
            }
        }
        return null;
    }

    public Mine getByLocation(Block b) {
        return this.mineLocationCache.get(this.getLocationHashKey(b.getX()));
    }


    private int getLocationHashKey(Mine mine) {
        return this.getLocationHashKey(mine.getMinX());
    }

    /**
     * Uses a neat trick to quickly get a mine from a location.
     * Since mines are stored 2^x blocks apart. We can simply use
     * the Mine's x value {@code mineX >> x}
     *
     * @param x The location's x to be hashed
     * @return The hashkey of the mine to be used in {@code mineLocationCache}
     */
    private int getLocationHashKey(int x) {
        return x >> MinesWorldManager.WORLD_GAP;
    }

}
