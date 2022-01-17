package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.entity.farming.FarmingConf;
import com.massivecraft.massivecore.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class EngineFarming extends Engine {

    private static final EngineFarming i = new EngineFarming();

    public static EngineFarming get() {
        return i;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent evt) {
        FarmingConf.get().onWheatBreak(evt);
    }

}
