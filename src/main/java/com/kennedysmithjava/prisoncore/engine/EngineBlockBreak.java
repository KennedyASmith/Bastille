package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.blockhandler.MineRegionCache;
import com.kennedysmithjava.prisoncore.util.MinesWorldManager;
import com.kennedysmithjava.prisoncore.event.MineBlockBreakEvent;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.util.regions.LazyRegion;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class EngineBlockBreak extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MineRegionCache cache = MineRegionCache.get();

    private static final EngineBlockBreak i = new EngineBlockBreak();

    public EngineBlockBreak() {
    }

    public static EngineBlockBreak get() {
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
            if(event.getBlock().getWorld().getName().equals(MinesWorldManager.getWorldName())){
                event.setCancelled(!event.getPlayer().isOp());
            }
            return;
        }

        if (!region.contains(event.getBlock())) {
            // No mine region exists there, we'll let something else handle the block breaking.
            if(event.getBlock().getWorld().getName().equals(MinesWorldManager.getWorldName())){
                event.setCancelled(!event.getPlayer().isOp());
            }
            return;
        }

        Distribution distribution = cache.getDistribution(event.getBlock());

        // We'll handle the block break ourselves to prevent lag.
        event.setCancelled(true);

        MineBlockBreakEvent customEvent = new MineBlockBreakEvent(event, region, distribution);
        Bukkit.getPluginManager().callEvent(customEvent);

    }


}
