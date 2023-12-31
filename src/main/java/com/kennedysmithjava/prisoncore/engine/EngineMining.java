package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.blockhandler.MineRegionCache;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.event.EventMineBlockBreak;
import com.kennedysmithjava.prisoncore.regions.LazyRegion;
import com.kennedysmithjava.prisoncore.regions.MinesWorldManager;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class EngineMining extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MineRegionCache cache = MineRegionCache.get();

    private static final EngineMining i = new EngineMining();

    public EngineMining() {
    }

    public static EngineMining get() {
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

        EventMineBlockBreak customEvent = new EventMineBlockBreak(event, region, distribution);
        Bukkit.getPluginManager().callEvent(customEvent);
    }


}
