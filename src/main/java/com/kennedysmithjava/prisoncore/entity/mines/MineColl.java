package com.kennedysmithjava.prisoncore.entity.mines;

import com.kennedysmithjava.prisoncore.regions.MinesWorldManager;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;
import com.sk89q.worldedit.bukkit.fastutil.ints.Int2ObjectArrayMap;
import com.sk89q.worldedit.bukkit.fastutil.ints.Int2ObjectMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        addMineToCache(entity);
    }

    public void addMineToCache(Mine mine){
        Bukkit.getScheduler()
                .scheduleSyncDelayedTask(PrisonCore.get(),
                        () -> this.mineLocationCache.put(this.getLocationHashKey(mine), mine),
                        20L);
    }

    public void removeMineFromCache(Mine mine){
        Bukkit.getScheduler()
                .scheduleSyncDelayedTask(PrisonCore.get(),
                        () -> this.mineLocationCache.remove(this.getLocationHashKey(mine)),
                        20L);
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
        return this.getByLocation(b.getLocation());
    }

    public Mine getByLocation(Location l) {
        return this.mineLocationCache.get(this.getLocationHashKey(l.getBlockX()));
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
