package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.Tree;
import com.massivecraft.massivecore.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class EngineTrees extends Engine {

    public static EngineTrees i = new EngineTrees();

    public static EngineTrees get() { return i; }

    /**
     * Handles blocks on trees being removed
     * @param evt event
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent evt) {
        Tree tree = TreesConf.get().getTree(evt.getBlock());

        if(tree == null) {
            return;
        }

        try {
            TreesConf.get().chopTree(tree, evt.getPlayer(), evt);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
