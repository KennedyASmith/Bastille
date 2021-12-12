package com.kennedysmithjava.prisonmines.blockhandler;

import com.kennedysmithjava.prisonmines.blockhandler.event.BlockEventFulfiller;
import com.kennedysmithjava.prisonmines.blockhandler.event.MineBlockBreakEvent;
import com.kennedysmithjava.prisonmines.engine.ResearchPointEngine;
import com.kennedysmithjava.prisonmines.entity.Distribution;
import com.kennedysmithjava.prisonmines.util.LazyRegion;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakEngine extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MineRegionCache cache = MineRegionCache.get();
    private static final BlockEventFulfiller fulfiller = BlockEventFulfiller.get();

    private static final BlockBreakEngine i = new BlockBreakEngine();

    public BlockBreakEngine() {
    }

    public static BlockBreakEngine get() {
        return i;
    }

    // -------------------------------------------- //
    // EVENTS
    // -------------------------------------------- //
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        // Mine region for the Mine, not null unless mine itself isnt cached.
        LazyRegion region = cache.getRegion(event.getBlock());

        // We may need to cache this region if it doesn't exist!
        if (region == null) {
            // Try and find it in the cache, if it's found restart from the top of this method.
            if (cache.tryCache(event.getBlock())) {
                this.onBlockBreak(event);
            }

            // No mine region exists there, we'll let something else handle the block breaking.
            return;
        }

        if (!region.contains(event.getBlock())) {
            // No mine region exists there, we'll let something else handle the block breaking.

            return;
        }

        Distribution distribution = cache.getDistribution(event.getBlock());

        /*
         TODO: Potentially add some permission requirements here to prevent players from mining
               in another person's Mine when we don't want them to.
         */

        // We'll handle the block break ourselves to prevent lag.
        event.setCancelled(true);

        MineBlockBreakEvent customEvent = new MineBlockBreakEvent(event, region, distribution);
        Bukkit.getPluginManager().callEvent(customEvent);

        fulfiller.handleEventReturn(customEvent);
    }

}
