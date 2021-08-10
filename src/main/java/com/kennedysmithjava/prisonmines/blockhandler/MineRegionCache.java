package com.kennedysmithjava.prisonmines.blockhandler;

import com.boydti.fawe.object.collection.SoftHashMap;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.entity.blocks.Distribution;
import com.kennedysmithjava.prisonmines.entity.blocks.DistributionConf;
import com.kennedysmithjava.prisonmines.entity.mine.Mine;
import com.kennedysmithjava.prisonmines.entity.mine.MineColl;
import com.kennedysmithjava.prisonmines.entity.mine.MinesConf;
import com.kennedysmithjava.prisonmines.util.LazyRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MineRegionCache {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MineRegionCache i = new MineRegionCache();
    public static MineRegionCache get() {
        return i;
    }

    private MineRegionCache() {
        this.mineRegionCache = new SoftHashMap<>(300);
        this.alternativelyCachedRegions = new HashSet<>(50);
        this.distributionMap = new HashMap<>();
    }

    // -------------------------------------------- //
    // DATA
    // -------------------------------------------- //

    // We can use soft references here as we are being careful not to rely on it being in the cache.
    // That way, if memory gets a bit tight, we can offload unused members of this cache for other content.
    private final SoftHashMap<Integer, LazyRegion> mineRegionCache;

    private final Map<LazyRegion, Distribution> distributionMap;

    private final Set<LazyRegion> alternativelyCachedRegions;

    private final String minesWorldName = MinesConf.get().minesWorldName;

    // -------------------------------------------- //
    // IMPL
    // -------------------------------------------- //

    /**
     * Check the MineWorldManager to see if there is a region at this block since it doesn't exist in the cache
     *
     * @return true if it was added to the cache, false otherwise.
     */
    public boolean tryCache(Block block) {
        // No need to check if it's already in the cache as presumably this method wouldn't be called otherwise.

        // Get the mine from the location cache in MineColl
        Mine mine = MineColl.get().getByLocation(block);

        if (mine == null) {
            // No mine was found in the specified location. Return false.
            return false;
        }

        LazyRegion lazyRegion = mine.getLazyRegion();
        Distribution distribution = DistributionConf.get().distribution.get(mine.getCurrentDistributionID());

        this.cacheRegion(lazyRegion, distribution);

        return true;
    }


    public void cacheRegion(LazyRegion region, Distribution distribution) {
        // Let's make the distibutions accessible via HashMap to get O(1) lookup times on them.
        this.distributionMap.put(region, distribution);

        int key = this.getKey(region.getMinX());
        int validationKey = this.getKey(region.getMaxX());

        if (key != validationKey || !region.getWorld().equals(this.minesWorldName)) {
            // This indicates that a mine is being cached which isn't compatible with.
            // The caching algorithm and so we will store these in a seperate cache that will be iterated upon.
            // However, if this occurs alot it could be that the regions in the mine world aren't being cached
            // appropriately.
            PrisonMines.get().getLogger().info("Cached Mine region into alternate cache. "
                    + "If you are seeing this frequently, it could indicate a problem with the cache");

            this.alternativelyCachedRegions.add(region);
            return;
        }

        // If this happens it means that a problem exists in finding a key for the caching algorithm or there are
        // two mine regions in one Mine. If this warning is found it is advisable to create a fix, otherwise the
        // cache won't be as effective.
        if (this.mineRegionCache.containsKey(key)) {
            PrisonMines.get().getLogger().warning("LazyRegion: " + region
                    + " clashes with another region already in the cache");
        }

        this.mineRegionCache.put(key, region);
    }

    public Distribution getDistribution(LazyRegion region) {
        return this.distributionMap.get(region);
    }

    public Distribution getDistribution(Block b) {
        return this.getDistribution(this.getRegion(b));
    }

    public LazyRegion getRegion(Block b) {
        return this.mineRegionCache.getOrDefault(this.getKey(b),
                this.alternativelyCachedRegions.stream().filter(r -> r.contains(b)).findFirst().orElse(null)) ;
    }

    public boolean isInRegion(Block b) {
        return this.mineRegionCache.get(this.getKey(b)) != null
                || this.alternativelyCachedRegions.stream().anyMatch(r -> r.contains(b));
    }

    public int getKey(Location location) {
        return this.getKey(location.getBlockX());
    }

    public int getKey(Block block) {
        return this.getKey(block.getX());
    }

    public int getKey(int x) {
        return x << 8;
    }

}
