package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.blockhandler.MineRegionCache;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.event.EventMineChanged;
import com.massivecraft.massivecore.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class EngineCacheUpdate extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MineRegionCache cache = MineRegionCache.get();

    private static final EngineCacheUpdate i = new EngineCacheUpdate();

    public EngineCacheUpdate() {
    }

    public static EngineCacheUpdate get() {
        return i;
    }

    // -------------------------------------------- //
    // EVENTS
    // -------------------------------------------- //

    /**
     * This event is fired when distributions and other mine features
     * are changed. When such events happen we want to make sure the
     * cache isn't storing incorrect values that produce undesirable effects.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onMineChanged(EventMineChanged event) {
        final Mine mine = event.getMine();

        cache.invalidate(mine.getLazyRegion());
    }

}
