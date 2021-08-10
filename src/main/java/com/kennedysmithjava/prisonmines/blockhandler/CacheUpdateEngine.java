package com.kennedysmithjava.prisonmines.blockhandler;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.event.EventMineChanged;
import com.massivecraft.massivecore.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class CacheUpdateEngine extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MineRegionCache cache = MineRegionCache.get();

    private static final CacheUpdateEngine i = new CacheUpdateEngine();

    public CacheUpdateEngine() {
    }

    public static CacheUpdateEngine get() {
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
